package com.solo.Personalproject.constant;

public enum Category {
    LIQUID_PHASE("액상"),
    MACHINE("기기"),
    CONSUMABLES("소모품"); // 액상 , 기기 , 소모품


    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
