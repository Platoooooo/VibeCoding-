@echo off
chcp 65001 >nul
echo ========================================
echo 企业信息展示小程序后端 - Windows部署脚本
echo ========================================
echo.

REM 检查管理员权限
net session >nul 2>&1
if %errorLevel% neq 0 (
    echo [错误] 请以管理员身份运行此脚本
    echo 右键点击脚本，选择"以管理员身份运行"
    pause
    exit /b 1
)

echo [1/5] 检查 Java 环境...
java -version >nul 2>&1
if %errorLevel% neq 0 (
    echo [错误] 未检测到 Java 环境
    echo 请先安装 JDK 17 或更高版本
    echo 下载地址: https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)
echo [成功] Java 环境正常
echo.

echo [2/5] 检查 Maven 环境...
call mvn -version >nul 2>&1
if %errorLevel% neq 0 (
    echo [警告] 未检测到 Maven，将使用内置启动方式
    set USE_MAVEN=false
) else (
    echo [成功] Maven 环境正常
    set USE_MAVEN=true
)
echo.

echo [3/5] 编译项目...
if "%USE_MAVEN%"=="true" (
    call mvn clean package -DskipTests
    if %errorLevel% neq 0 (
        echo [错误] 编译失败
        pause
        exit /b 1
    )
    echo [成功] 编译完成
) else (
    echo [提示] 跳过Maven编译，使用已编译的文件
)
echo.

echo [4/5] 配置防火墙规则...
netsh advfirewall firewall show rule name="WXZZ-Backend-8080" >nul 2>&1
if %errorLevel% equ 0 (
    echo [提示] 防火墙规则已存在
) else (
    netsh advfirewall firewall add rule name="WXZZ-Backend-8080" dir=in action=allow protocol=TCP localport=8080 >nul 2>&1
    echo [成功] 已添加防火墙规则，允许端口 8080
)
echo.

echo [5/5] 安装 Windows 服务...
echo.
echo 请选择安装方式:
echo   1. 安装为 Windows 服务（推荐，开机自启）
echo   2. 仅创建启动脚本（手动启动）
echo   3. 退出
echo.
set /p choice="请输入选择 (1/2/3): "

if "%choice%"=="1" (
    call :install_service
) else if "%choice%"=="2" (
    call :create_startup_script
) else (
    echo 已取消部署
    pause
    exit /b 0
)

goto :end

:install_service
echo.
echo ========================================
echo 安装 Windows 服务
echo ========================================
echo.

REM 检查是否已存在服务
sc query WXZZBackend >nul 2>&1
if %errorLevel% equ 0 (
    echo [提示] 服务已存在，是否重新安装？
    set /p reinstall="是否重新安装？(Y/N): "
    if /i "%reinstall%"=="Y" (
        net stop WXZZBackend >nul 2>&1
        sc delete WXZZBackend >nul 2>&1
        echo [成功] 旧服务已删除
    ) else (
        echo 已取消安装
        goto :end
    )
)

REM 获取当前目录
set CURRENT_DIR=%~dp0
set JAR_PATH=%CURRENT_DIR%target\wxzz-prod-1.0.0.jar

REM 检查 JAR 文件是否存在
if not exist "%JAR_PATH%" (
    echo [错误] 找不到 JAR 文件: %JAR_PATH%
    echo 请先执行编译: mvn clean package
    pause
    exit /b 1
)

REM 使用 NSSM 或 WinSW 安装服务
echo.
echo 请选择服务安装工具:
echo   1. 使用批处理后台运行（简单方式）
echo   2. 使用 PowerShell 任务计划程序（高级方式）
echo.
set /p service_type="请输入选择 (1/2): "

if "%service_type%"=="1" (
    call :install_simple_service
) else if "%service_type%"=="2" (
    call :install_scheduled_task
) else (
    echo 无效选择
    goto :end
)

goto :end

:install_simple_service
echo.
echo ========================================
echo 创建后台服务模式
echo ========================================
echo.

REM 创建服务启动脚本
(
echo @echo off
echo chcp 65001 ^>nul
echo title WXZZ Backend Service
echo set CURRENT_DIR=%%~dp0
echo cd /d "%%CURRENT_DIR%%"
echo echo %%date%% %%time%% - 服务启动 ^>^> service.log
echo java -jar -Dserver.port=8080 target\wxzz-prod-1.0.0.jar ^>^> service.log 2^>^&1
) > "%CURRENT_DIR%service-start.bat"

REM 创建服务停止脚本
(
echo @echo off
echo title Stopping WXZZ Backend Service
echo echo 正在停止服务...
echo for /f "tokens=5" %%%%a in ^('netstat -ano ^| findstr :8080'^) do ^(
echo     taskkill /F /PID %%%%a ^>nul 2^>^&1
echo ^)
echo echo 服务已停止
echo timeout /t 2 ^>nul
) > "%CURRENT_DIR%service-stop.bat"

REM 创建 VBScript 用于隐藏窗口启动
(
echo Set WshShell = CreateObject^("WScript.Shell"^)
echo WshShell.Run chr^(34^) ^& "%CURRENT_DIR%service-start.bat" ^& Chr^(34^), 0
echo Set WshShell = Nothing
) > "%CURRENT_DIR%service-start.vbs"

echo [成功] 服务脚本已创建
echo.
echo 服务管理命令:
echo   启动服务: 双击 service-start.vbs
echo   停止服务: 双击 service-stop.bat
echo   查看日志: 打开 service.log 文件
echo.

REM 添加到启动项
set STARTUP_FOLDER=%APPDATA%\Microsoft\Windows\Start Menu\Programs\Startup
copy /Y "%CURRENT_DIR%service-start.vbs" "%STARTUP_FOLDER%\WXZZBackend.vbs" >nul 2>&1
echo [成功] 已添加到开机启动项
echo.

goto :end

:install_scheduled_task
echo.
echo ========================================
echo 使用任务计划程序创建服务
echo ========================================
echo.

set CURRENT_DIR=%~dp0

REM 删除已存在的任务
schtasks /delete /tn "WXZZBackend" /f >nul 2>&1

REM 创建任务计划
schtasks /create /tn "WXZZBackend" /tr "\"%CURRENT_DIR%service-start.bat\"" /sc onstart /ru SYSTEM /rl HIGHEST /f >nul 2>&1

if %errorLevel% equ 0 (
    echo [成功] 任务计划已创建
    echo 服务将在系统启动时自动运行
    echo.
    echo 手动启动命令: schtasks /run /tn "WXZZBackend"
    echo 手动停止命令: schtasks /delete /tn "WXZZBackend" /f
) else (
    echo [错误] 任务计划创建失败
)
echo.

goto :end

:create_startup_script
echo.
echo ========================================
echo 创建启动脚本
echo ========================================
echo.

set CURRENT_DIR=%~dp0

REM 创建快速启动脚本
(
echo @echo off
echo chcp 65001 ^>nul
echo title WXZZ Backend - Port 8080
echo cd /d "%%~dp0"
echo echo ========================================
echo echo 企业信息展示小程序后端
echo echo 端口: 8080
echo echo ========================================
echo echo.
echo echo 正在启动服务...
echo java -jar -Dserver.port=8080 target\wxzz-prod-1.0.0.jar
echo.
echo pause
) > "%CURRENT_DIR%run.bat"

echo [成功] 启动脚本已创建: run.bat
echo.
echo 双击 run.bat 即可启动服务
echo.

goto :end

:end
echo.
echo ========================================
echo 部署完成！
echo ========================================
echo.
echo 访问地址:
echo   后端 API: http://localhost:8080
echo   管理后台: http://localhost:8080/admin/index.html
echo.
echo 默认账号:
echo   用户名: admin
echo   密码: admin123
echo.
pause
