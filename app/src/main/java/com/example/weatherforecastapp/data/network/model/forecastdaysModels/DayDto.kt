package com.example.testapi.network.model.forecastdaysModels

import com.example.testapi.network.model.curentModels.ConditionDto
import com.google.gson.annotations.SerializedName



data class DayDto(
    @SerializedName("maxtemp_c") val maxtempC: Double, // Максимальная температура в градусах Цельсия
    //@SerializedName("maxtemp_f") val maxtempF: Double, // Максимальная температура в градусах Фаренгейта
    @SerializedName("mintemp_c") val mintempC: Double, // Минимальная температура в градусах Цельсия
 //   @SerializedName("mintemp_f") val mintempF: Double, // Минимальная температура в градусах Фаренгейта
    @SerializedName("avgtemp_c") val avgtempC: Double, // Средняя температура в градусах Цельсия
  //  @SerializedName("avgtemp_f") val avgtempF: Double, // Средняя температура в градусах Фаренгейта
   // @SerializedName("maxwind_mph") val maxwindMph: Double, // Максимальная скорость ветра в милях в час
    @SerializedName("maxwind_kph") val maxwindKph: Double, // Максимальная скорость ветра в километрах в час
    @SerializedName("totalprecip_mm") val totalprecipMm: Double, // Общее количество осадков в миллиметрах
  //  @SerializedName("totalprecip_in") val totalprecipIn: Double, // Общее количество осадков в дюймах
    @SerializedName("totalsnow_cm") val totalsnowCm: Double, // Общее количество снега в сантиметрах
    @SerializedName("avgvis_km") val avgvisKm: Double, // Средняя видимость в километрах
   // @SerializedName("avgvis_miles") val avgvisMiles: Double, // Средняя видимость в милях
    @SerializedName("avghumidity") val avghumidity: Int, // Средняя влажность воздуха в процентах
    @SerializedName("daily_will_it_rain") val dailyWillItRain: Int, // Будет ли дождь (0 - нет, 1 - да)
    @SerializedName("daily_chance_of_rain") val dailyChanceOfRain: Int, // Вероятность дождя в процентах
    @SerializedName("daily_will_it_snow") val dailyWillItSnow: Int, // Будет ли снег (0 - нет, 1 - да)
    @SerializedName("daily_chance_of_snow") val dailyChanceOfSnow: Int, // Вероятность снега в процентах
    @SerializedName("condition") val condition: ConditionDto, // Условия погоды
  //  @SerializedName("uv") val uv: Double // Уровень ультрафиолетового излучения
)


