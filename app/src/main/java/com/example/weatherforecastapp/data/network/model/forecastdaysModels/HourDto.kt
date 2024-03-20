package com.example.testapi.network.model.forecastdaysModels

import com.example.testapi.network.model.curentModels.ConditionDto
import com.google.gson.annotations.SerializedName


data class HourDto(
  // @SerializedName("time_epoch") val timeEpoch: Long, // Время в формате Unix Epoch
    @SerializedName("time") val time: String, // Время
    @SerializedName("temp_c") val tempC: Double, // Температура в градусах Цельсия
   // @SerializedName("temp_f") val tempF: Double, // Температура в градусах Фаренгейта
    @SerializedName("is_day") val isDay: Int, // Показывает, день или ночь (0 - ночь, 1 - день)
    @SerializedName("condition") val condition: ConditionDto, // Условия погоды
   // @SerializedName("wind_mph") val windMph: Double, // Скорость ветра в милях в час
   // @SerializedName("wind_kph") val windKph: Double, // Скорость ветра в километрах в час
    //@SerializedName("wind_degree") val windDegree: Int, // Направление ветра в градусах
   // @SerializedName("wind_dir") val windDir: String, // Направление ветра
   // @SerializedName("pressure_mb") val pressureMb: Double, // Давление в миллибарах
   // @SerializedName("pressure_in") val pressureIn: Double, // Давление в дюймах ртутного столба
   // @SerializedName("precip_mm") val precipMm: Double, // Количество осадков в миллиметрах
   // @SerializedName("precip_in") val precipIn: Double, // Количество осадков в дюймах
   // @SerializedName("snow_cm") val snowCm: Double, // Количество снега в сантиметрах
   // @SerializedName("humidity") val humidity: Int, // Влажность воздуха в процентах
   // @SerializedName("cloud") val cloud: Int, // Облачность в процентах
    @SerializedName("feelslike_c") val feelslikeC: Double, // Ощущаемая температура в градусах Цельсия
  //  @SerializedName("feelslike_f") val feelslikeF: Double, // Ощущаемая температура в градусах Фаренгейта
  //  @SerializedName("windchill_c") val windchillC: Double, // Ощущаемая температура ветра в градусах Цельсия
  //  @SerializedName("windchill_f") val windchillF: Double, // Ощущаемая температура ветра в градусах Фаренгейта
  //  @SerializedName("heatindex_c") val heatindexC: Double, // Температурный индекс в градусах Цельсия
  //  @SerializedName("heatindex_f") val heatindexF: Double, // Температурный индекс в градусах Фаренгейта
  //  @SerializedName("dewpoint_c") val dewpointC: Double, // Точка росы в градусах Цельсия
  //  @SerializedName("dewpoint_f") val dewpointF: Double, // Точка росы в градусах Фаренгейта
  //  @SerializedName("will_it_rain") val willItRain: Int, // Будет ли дождь (0 - нет, 1 - да)
  //  @SerializedName("chance_of_rain") val chanceOfRain: Int, // Вероятность дождя в процентах
  //  @SerializedName("will_it_snow") val willItSnow: Int, // Будет ли снег (0 - нет, 1 - да)
  //  @SerializedName("chance_of_snow") val chanceOfSnow: Int, // Вероятность снега в процентах
  //  @SerializedName("vis_km") val visKm: Double, // Видимость в километрах
  //  @SerializedName("vis_miles") val visMiles: Double, // Видимость в милях
  //  @SerializedName("gust_mph") val gustMph: Double, // Порывы ветра в милях в час
  //  @SerializedName("gust_kph") val gustKph: Double, // Порывы ветра в километрах в час
  //  @SerializedName("uv") val uv: Double, // Ультрафиолетовый индекс
  //  @SerializedName("short_rad") val shortRad: Double
)
