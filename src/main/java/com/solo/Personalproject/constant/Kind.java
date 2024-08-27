package com.solo.Personalproject.constant;

public enum Kind {
    //액상
    M_BREATHE("입호흡 액상"),
    P_BREATHE("폐호흡 액상"),
    M_MACHINE("입호흡 기기"),
    P_MACHINE("폐호흡 기기"),
    O_MACHINE("일회용 기기"),
    FRUIT("과일 액상"),
    MENSOLE("맨솔 액상"),
    DESSERT("디저트 액상"),
    BEVERAGE("음료 액상"),
    CIGARETTE("연초 액상"),
    //입호흡 기기
    VALYRIAN("발라리안"),
    VOOPOO("부푸"),
    GECK_VAPE("긱베이프"),
    VAPORESSO("베이포레소"),
    HANYA("한야"),
    //폐호흡 기기
    DOT_MOD("닷모드"),
    VAPELUSTION("베이프루션"),
    DOPO("도포"),
    ASPIRE("아스파이어"),
    RINCOE("린코"),
    //일회용 기기
    PUFF_SALT("퍼프 솔트"),
    NASTY_FIXGO("네스티 픽스고"),
    ORCA_AIR("오르카 에어"),
    //소모품
    POT("팟"),
    COIL("코일"),
    TANK("탱크"),
    BATTERY("배터리"),
    ACCESSOIRES("악세서리");

    private String displayName;

    Kind(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
}
