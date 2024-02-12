package com.corylustech.superai.presentation.wallpaper.model

data class ColorModel(
    val coloname:String,
    val colocode: String
)

 fun setupColors(): ArrayList<ColorModel> {
    val colors = ArrayList<ColorModel>()
    colors.add(ColorModel("red", "#FF0000"))
    colors.add(ColorModel("orange", "#FFA500"))
    colors.add(ColorModel("yellow", "#FFFF00"))
    colors.add(ColorModel("green", "#00FF00"))
    colors.add(ColorModel("cyan", "#00FFFF"))
    colors.add(ColorModel("blue", "#0000FF"))
    colors.add(ColorModel("magenta", "#FF00FF"))
    colors.add(ColorModel("purple", "#800080"))
    colors.add(ColorModel("white", "#FFFFFF"))
    colors.add(ColorModel("black", "#000000"))
    colors.add(ColorModel("violet", "#8F00FF"))
    colors.add(ColorModel("indigo", "#4B0082"))
    return colors
}
