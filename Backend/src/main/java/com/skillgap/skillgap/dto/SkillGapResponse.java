package com.skillgap.skillgap.dto;

import java.util.List;

public class SkillGapResponse {

    private List<String> strongSkills;
    private List<String> weakSkills;
    private List<String> missingSkills;

    public SkillGapResponse(List<String> strongSkills,
                            List<String> weakSkills,
                            List<String> missingSkills) {
        this.strongSkills = strongSkills;
        this.weakSkills = weakSkills;
        this.missingSkills = missingSkills;
    }

    public List<String> getStrongSkills() {
        return strongSkills;
    }

    public List<String> getWeakSkills() {
        return weakSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }
}