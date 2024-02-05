package model

class Contact() {
    var id: Int = 0
    var firstName = ""
    var lastName = ""
    var number = ""
    constructor(FName: String, LName: String, no: String):this()
    {
        firstName = FName
        lastName = LName
        number = no
    }
}