package com.example.testapi.network.model.curentModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ConditionDto(
    @SerializedName("text") @Expose val text: String,
    @SerializedName("icon") @Expose val icon: String,
    @SerializedName("code") @Expose val code: Int
)
