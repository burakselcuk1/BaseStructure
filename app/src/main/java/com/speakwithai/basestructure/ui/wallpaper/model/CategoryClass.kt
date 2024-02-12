package com.corylustech.superai.presentation.wallpaper.model

data class CategoryClass(
    val categoryname:String,
    val categoryurl:String
)

 fun setupCategory():ArrayList<ArrayList<CategoryClass>>{
    var b=ArrayList<ArrayList<CategoryClass>>()

    var temp=ArrayList<CategoryClass>()
    temp.add(CategoryClass("Art","https://images.pexels.com/photos/3246665/pexels-photo-3246665.png?auto=compress&cs=tinysrgb&h=650&w=940"))
    temp.add(CategoryClass("Ocean","https://images.pexels.com/photos/189349/pexels-photo-189349.jpeg?auto=compress\\u0026cs=tinysrgb\\u0026h=650\\u0026w=940"))
    b.add(temp)

    temp=ArrayList<CategoryClass>()
    temp.add(CategoryClass("Bicycle","https://images.pexels.com/photos/5465176/pexels-photo-5465176.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"))
    temp.add(CategoryClass("Bikes","https://images.pexels.com/photos/2949302/pexels-photo-2949302.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"))
    b.add(temp)

    temp=ArrayList<CategoryClass>()
    temp.add(CategoryClass("Flowers","https://images.pexels.com/photos/931177/pexels-photo-931177.jpeg?auto=compress\\u0026cs=tinysrgb\\u0026h=650\\u0026w=940"))
    temp.add(CategoryClass("Cars","https://images.pexels.com/photos/170811/pexels-photo-170811.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"))
    b.add(temp)

    temp=ArrayList<CategoryClass>()
    temp.add(CategoryClass("Entertainment","https://images.pexels.com/photos/1763075/pexels-photo-1763075.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"))
    temp.add(CategoryClass("Gaming","https://images.pexels.com/photos/3165335/pexels-photo-3165335.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"))
    b.add(temp)

    temp=ArrayList<CategoryClass>()
    temp.add(CategoryClass("Gods","https://images.pexels.com/photos/2969469/pexels-photo-2969469.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"))
    temp.add(CategoryClass("Travel","https://images.pexels.com/photos/2325446/pexels-photo-2325446.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"))
    b.add(temp)

    temp=ArrayList<CategoryClass>()
    temp.add(CategoryClass("Food","https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"))
    temp.add(CategoryClass("Music","https://images.pexels.com/photos/6966/abstract-music-rock-bw.jpg?auto=compress&cs=tinysrgb&h=650&w=940"))
    b.add(temp)

    temp=ArrayList<CategoryClass>()
    temp.add(CategoryClass("Nature","https://images.pexels.com/photos/624015/pexels-photo-624015.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"))
    temp.add(CategoryClass("Neon","https://images.pexels.com/photos/5411700/pexels-photo-5411700.png?auto=compress&cs=tinysrgb&h=650&w=940"))
    b.add(temp)

    temp=ArrayList<CategoryClass>()
    temp.add(CategoryClass("Space","https://images.pexels.com/photos/220201/pexels-photo-220201.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"))
    temp.add(CategoryClass("Rain","https://images.pexels.com/photos/1100946/pexels-photo-1100946.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"))
    b.add(temp)

    temp=ArrayList<CategoryClass>()
    temp.add(CategoryClass("Sports","https://images.pexels.com/photos/46798/the-ball-stadion-football-the-pitch-46798.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"))
    temp.add(CategoryClass("Wildlife","https://images.pexels.com/photos/247431/pexels-photo-247431.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"))
    b.add(temp)

    return  b
}

