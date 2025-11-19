package com.example.mobileapp.data.model

class CarModel {
    val carName: String;
    val job: String;
    val comment: String;
    val image: Int;

    constructor(_carName: String, _job: String, _comment: String, _image: Int){
        carName = _carName
        job = _job
        comment = _comment
        image = _image
    }
}