package com.example.weatherforecastapp.data.database.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "current_day",
    foreignKeys = [
        ForeignKey(
            entity = LocationDb::class,
            parentColumns = ["position_id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CurrentDb(
    @PrimaryKey
    val id: Int,
    val nameCity: String,
    val date: String, // Дата прогноза
    val last_updated_epoch: Long, // Время последнего обновления погоды в формате Unix Epoch
    val last_updated: String, // Дата и время последнего обновления погоды
    val temp_c: Double, // Температура в градусах Цельсия
    val is_day: Int, // Показывает, является ли сейчас день (1 - да, 0 - нет)
    val param_windKph: Double,// Скорость ветра в километрах в час
    val param_windDegree: Int,// Направление ветра в градусах
    val param_pressureIn: Double,// Давление в дюймах ртутного столба
    val param_precipitationMm: Double,// Количество осадков в миллиметрах
    val param_humidity: Int,// Влажность в процентах
    val param_cloud: Int,// Облачность в процентах
    val param_feelsLikeCelsius: Double,// Ощущаемая температура в градусах Цельсия
    val param_visibilityKm: Double,// Видимость в километрах
    val param_uvIndex: Double,// Ультрафиолетовый индекс
    val param_gustKph: Double,// Скорость порывов ветра в километрах в час
    val day_maxtempC: Double, // Максимальная температура в градусах Цельсия
    val day_mintempC: Double, // Минимальная температура в градусах Цельсия
    val day_avgtempC: Double, // Средняя температура в градусах Цельсия
    val day_maxwindKph: Double, // Максимальная скорость ветра в километрах в час
    val day_totalprecipMm: Double, // Общее количество осадков в миллиметрах
    val day_totalsnowCm: Double, // Общее количество снега в сантиметрах
    val day_avgvisKm: Double, // Средняя видимость в километрах
    val day_avghumidity: Int, // Средняя влажность воздуха в процентах
    val day_dailyWillItRain: Int, // Будет ли дождь (0 - нет, 1 - да)
    val day_dailyChanceOfRain: Int, // Вероятность дождя в процентах
    val day_dailyWillItSnow: Int, // Будет ли снег (0 - нет, 1 - да)
    val day_dailyChanceOfSnow: Int, // Вероятность снега в процентах
    val astro_sunrise: String, // Время восхода солнца
    val astro_sunset: String, // Время заката солнца
    val astro_moonrise: String, // Время восхода луны
    val astro_moonset: String, // Время заката луны
    val astro_moonPhase: String, // Фаза луны
    val astro_moonIllumination: Int, // Освещенность луны в процентах
    val astro_isMoonUp: Int, // Показывает, восходит ли луна (0 - нет, 1 - да)
    val astro_isSunUp: Int,// Показывает, восходит ли солнце (0 - нет, 1 - да)
    val condition_text: String,
    val condition_icon: String,
    val condition_code: Int
)