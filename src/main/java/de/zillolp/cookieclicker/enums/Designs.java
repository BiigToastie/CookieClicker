package de.zillolp.cookieclicker.enums;

import de.zillolp.cookieclicker.config.LanguageTools;
import de.zillolp.cookieclicker.xclasses.XMaterial;

public enum Designs {
    BLACK_DESIGN(XMaterial.BLACK_STAINED_GLASS, XMaterial.BLACK_STAINED_GLASS_PANE, "BLACK_GLASS", "BLACK_GLASS_SELECTED"),
    GRAY_DESIGN(XMaterial.GRAY_STAINED_GLASS, XMaterial.GRAY_STAINED_GLASS_PANE, "GRAY_GLASS", "GRAY_GLASS_SELECTED"),
    LIGHT_GRAY_DESIGN(XMaterial.LIGHT_GRAY_STAINED_GLASS, XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE, "LIGHT_GRAY_GLASS", "LIGHT_GRAY_GLASS_SELECTED"),
    WHITE_DESIGN(XMaterial.WHITE_STAINED_GLASS, XMaterial.WHITE_STAINED_GLASS_PANE, "WHITE_GLASS", "WHITE_GLASS_SELECTED"),
    BLUE_DESIGN(XMaterial.BLUE_STAINED_GLASS, XMaterial.BLUE_STAINED_GLASS_PANE, "BLUE_GLASS", "BLUE_GLASS_SELECTED"),
    LIGHT_BLUE_DESIGN(XMaterial.LIGHT_BLUE_STAINED_GLASS, XMaterial.LIGHT_BLUE_STAINED_GLASS_PANE, "LIGHT_BLUE_GLASS", "LIGHT_BLUE_GLASS_SELECTED"),
    CYAN_DESIGN(XMaterial.CYAN_STAINED_GLASS, XMaterial.CYAN_STAINED_GLASS_PANE, "CYAN_GLASS", "CYAN_GLASS_SELECTED"),
    GREEN_DESIGN(XMaterial.GREEN_STAINED_GLASS, XMaterial.GREEN_STAINED_GLASS_PANE, "GREEN_GLASS", "GREEN_GLASS_SELECTED"),
    LIME_DESIGN(XMaterial.LIME_STAINED_GLASS, XMaterial.LIME_STAINED_GLASS_PANE, "LIME_GLASS", "LIME_GLASS_SELECTED"),
    BROWN_DESIGN(XMaterial.BROWN_STAINED_GLASS, XMaterial.BROWN_STAINED_GLASS_PANE, "BROWN_GLASS", "BROWN_GLASS_SELECTED"),
    PURPLE_DESIGN(XMaterial.PURPLE_STAINED_GLASS, XMaterial.PURPLE_STAINED_GLASS_PANE, "PURPLE_GLASS", "PURPLE_GLASS_SELECTED"),
    MAGENTA_DESIGN(XMaterial.MAGENTA_STAINED_GLASS, XMaterial.MAGENTA_STAINED_GLASS_PANE, "MAGENTA_GLASS", "MAGENTA_GLASS_SELECTED"),
    PINK_DESIGN(XMaterial.PINK_STAINED_GLASS, XMaterial.PINK_STAINED_GLASS_PANE, "PINK_GLASS", "PINK_GLASS_SELECTED"),
    YELLOW_DESIGN(XMaterial.YELLOW_STAINED_GLASS, XMaterial.YELLOW_STAINED_GLASS_PANE, "YELLOW_GLASS", "YELLOW_GLASS_SELECTED"),
    ORANGE_DESIGN(XMaterial.ORANGE_STAINED_GLASS, XMaterial.ORANGE_STAINED_GLASS_PANE, "ORANGE_GLASS", "ORANGE_GLASS_SELECTED"),
    RED_DESIGN(XMaterial.RED_STAINED_GLASS, XMaterial.RED_STAINED_GLASS_PANE, "RED_GLASS", "RED_GLASS_SELECTED");

    private final XMaterial type;
    private final XMaterial designType;
    private String name;
    private String selectedName;

    Designs(XMaterial type, XMaterial designType, String name, String selectedName) {
        this.type = type;
        this.designType = designType;
        this.name = name;
        this.selectedName = selectedName;
    }

    public void load() {
        name = LanguageTools.getLanguage(name);
        selectedName = LanguageTools.getLanguage(selectedName);
    }

    public XMaterial getType() {
        return type;
    }

    public XMaterial getDesignType() {
        return designType;
    }

    public String getName() {
        return name;
    }

    public String getSelectedName() {
        return selectedName;
    }
}
