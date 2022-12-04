package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.Campaign;
import com.bezkoder.springjwt.models.Tutorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CampaignRepositoy  extends JpaRepository<Campaign, Long> {
    @Modifying
    @Query(
            value = "truncate table campaign",
            nativeQuery = true
    )
    void truncateCampaign();
}

