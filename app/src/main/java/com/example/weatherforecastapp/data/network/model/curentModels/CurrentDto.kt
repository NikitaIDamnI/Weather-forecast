package com.example.testapi.network.model.curentModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CurrentDto(
// Время последнего обновления в формате эпохи (миллисекунды)
@SerializedName("last_updated_epoch") @Expose val lastUpdatedEpoch: Long,

// Время последнего обновления в текстовом формате
@SerializedName("last_updated") @Expose val lastUpdated: String,

// Температура в градусах Цельсия
@SerializedName("temp_c") @Expose val temperatureCelsius: Double,

// Температура в градусах Фаренгейта
@SerializedName("temp_f") @Expose val temperatureFahrenheit: Double,

// Флаг, указывающий, день или ночь
@SerializedName("is_day") @Expose val isDay: Int,

// Состояние погоды
@SerializedName("condition") @Expose val conditionDto: ConditionDto,

// Скорость ветра в милях в час
@SerializedName("wind_mph") @Expose val windMph: Double,

// Скорость ветра в километрах в час
@SerializedName("wind_kph") @Expose val windKph: Double,

// Направление ветра в градусах
@SerializedName("wind_degree") @Expose val windDegree: Int,

// Направление ветра в текстовом формате
@SerializedName("wind_dir") @Expose val windDirection: String,

// Давление в миллибарах
@SerializedName("pressure_mb") @Expose val pressureMb: Double,

// Давление в дюймах ртутного столба
@SerializedName("pressure_in") @Expose val pressureIn: Double,

// Количество осадков в миллиметрах
@SerializedName("precip_mm") @Expose val precipitationMm: Double,

// Количество осадков в дюймах
@SerializedName("precip_in") @Expose val precipitationIn: Double,

// Влажность в процентах
@SerializedName("humidity") @Expose val humidity: Int,

// Облачность в процентах
@SerializedName("cloud") @Expose val cloud: Int,

// Ощущаемая температура в градусах Цельсия
@SerializedName("feelslike_c") @Expose val feelsLikeCelsius: Double,

// Ощущаемая температура в градусах Фаренгейта
@SerializedName("feelslike_f") @Expose val feelsLikeFahrenheit: Double,

// Видимость в километрах
@SerializedName("vis_km") @Expose val visibilityKm: Double,

// Видимость в милях
@SerializedName("vis_miles") @Expose val visibilityMiles: Double,

// Ультрафиолетовый индекс
@SerializedName("uv") @Expose val uvIndex: Double,

// Скорость порывов ветра в милях в час
@SerializedName("gust_mph") @Expose val gustMph: Double,

// Скорость порывов ветра в километрах в час
@SerializedName("gust_kph") @Expose val gustKph: Double

)