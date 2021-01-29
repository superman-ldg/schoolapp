package com.example.schooassitant

data class Result(val result:Boolean)

data class Login(val uer_account:String,val user_account:String)

data class Book(val book_id:Int, val book_user_account:String, val book_name:String, val book_type:String, val book_information:String, val book_price:Int,
                val book_date: String,val book_state:Int)

data class Order(val order_id:Int,val order_user_account:String,val order_book_id:Int,val order_date:String)

data class Recruit(val recruit_id:Int,val recruit_title:String,val recruit_user:String,val recruit_user_account:String,val recruit_information:String,
                   val recruit_date:String)

data class User(val user_account:String,val user_password:String,val user_college:String,val user_nicheng:String,val user_email:String)
data class Picture(val name:String,val byte:ByteArray)