package com.jtoru.lab5firebase

import java.io.Serializable

class Item(
    var name : String ? = "",
    var amount : String ? = "",
    var description : String ? = "",
    var image : String ? = ""
): Serializable