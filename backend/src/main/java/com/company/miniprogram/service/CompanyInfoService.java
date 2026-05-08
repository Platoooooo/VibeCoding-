package com.company.miniprogram.service;

import com.company.miniprogram.model.CompanyInfo;
import com.company.miniprogram.repository.CompanyInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyInfoService {

    private final CompanyInfoRepository companyInfoRepository;

    public Optional<CompanyInfo> getCompanyInfo() {
        return companyInfoRepository.findAll().stream().findFirst();
    }

    @Transactional
    public CompanyInfo saveCompanyInfo(CompanyInfo companyInfo) {
        // 检查是否已存在公司信息
        Optional<CompanyInfo> existing = companyInfoRepository.findAll().stream().findFirst();
        if (existing.isPresent()) {
            // 更新现有记录
            CompanyInfo info = existing.get();
            info.setCompanyName(companyInfo.getCompanyName());
            info.setDescription(companyInfo.getDescription());
            info.setLogo(companyInfo.getLogo());
            info.setAddress(companyInfo.getAddress());
            info.setPhone(companyInfo.getPhone());
            info.setEmail(companyInfo.getEmail());
            info.setWechat(companyInfo.getWechat());
            info.setLatitude(companyInfo.getLatitude());
            info.setLongitude(companyInfo.getLongitude());
            info.setWorkdayTime(companyInfo.getWorkdayTime());
            info.setWeekendTime(companyInfo.getWeekendTime());
            return companyInfoRepository.save(info);
        } else {
            // 新增记录
            return companyInfoRepository.save(companyInfo);
        }
    }
}
