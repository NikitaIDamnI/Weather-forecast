package com.example.weatherforecastapp.data.mapper

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.testapi.network.model.forecastdaysModels.ForecastDayDto
import com.example.testapi.network.model.forecastdaysModels.HourDto
import com.example.testapi.network.model.searchCityModels.SearchCityDto
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.data.Format
import com.example.weatherforecastapp.data.database.models.CurrentDb
import com.example.weatherforecastapp.data.database.models.ForecastDaysDb
import com.example.weatherforecastapp.data.database.models.LocationDb
import com.example.weatherforecastapp.data.network.model.CityDto
import com.example.weatherforecastapp.domain.models.Astro
import com.example.weatherforecastapp.domain.models.City
import com.example.weatherforecastapp.domain.models.Condition
import com.example.weatherforecastapp.domain.models.Current
import com.example.weatherforecastapp.domain.models.Day
import com.example.weatherforecastapp.domain.models.ForecastDay
import com.example.weatherforecastapp.domain.models.ForecastDayCity
import com.example.weatherforecastapp.domain.models.ForecastHour
import com.example.weatherforecastapp.domain.models.Location
import com.example.weatherforecastapp.domain.models.SearchCity
import com.example.weatherforecastapp.domain.models.WeatherPrecipitation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject


class Mapper @Inject constructor() {
    fun mapperCityDtoToCurrentDb(cityDto: CityDto, positionId: Int) = CurrentDb(
        id = positionId,
        nameCity = cityDto.locationDto.name,
        date = Format.formatDate(cityDto.currentDto.lastUpdatedEpoch),
        last_updated_epoch = cityDto.currentDto.lastUpdatedEpoch,
        last_updated = cityDto.currentDto.lastUpdated,
        temp_c = cityDto.currentDto.temperatureCelsius,
        is_day = cityDto.currentDto.isDay,
        day_maxtempC = cityDto.forecast.days[0].day.maxtempC,
        day_mintempC = cityDto.forecast.days[0].day.maxtempC,
        day_avgtempC = cityDto.forecast.days[0].day.avgvisKm,
        day_maxwindKph = cityDto.forecast.days[0].day.maxwindKph,
        day_totalprecipMm = cityDto.forecast.days[0].day.totalprecipMm,
        day_totalsnowCm = cityDto.forecast.days[0].day.totalsnowCm,
        day_avgvisKm = cityDto.forecast.days[0].day.avgvisKm,
        day_avghumidity = cityDto.forecast.days[0].day.avghumidity,
        day_dailyWillItRain = cityDto.forecast.days[0].day.dailyWillItRain,
        day_dailyChanceOfRain = cityDto.forecast.days[0].day.dailyChanceOfRain,
        day_dailyWillItSnow = cityDto.forecast.days[0].day.dailyWillItSnow,
        day_dailyChanceOfSnow = cityDto.forecast.days[0].day.dailyChanceOfSnow,
        astro_sunrise = cityDto.forecast.days[0].astro.sunrise,
        astro_sunset = cityDto.forecast.days[0].astro.sunset,
        astro_moonrise = cityDto.forecast.days[0].astro.moonrise,
        astro_moonset = cityDto.forecast.days[0].astro.moonset,
        astro_moonPhase = cityDto.forecast.days[0].astro.moonPhase,
        astro_moonIllumination = cityDto.forecast.days[0].astro.moonIllumination,
        astro_isMoonUp = cityDto.forecast.days[0].astro.isSunUp,
        astro_isSunUp = cityDto.forecast.days[0].astro.isSunUp,
        condition_text = cityDto.currentDto.conditionDto.text,
        condition_icon = HTTPS_TEG + cityDto.currentDto.conditionDto.icon,
        condition_code = cityDto.currentDto.conditionDto.code,
        param_windKph = cityDto.currentDto.windKph,
        param_windDegree = cityDto.currentDto.windDegree,
        param_pressureMb = cityDto.currentDto.pressureMb,
        param_precipitationMm = cityDto.currentDto.precipitationMm,
        param_humidity = cityDto.currentDto.humidity,
        param_cloud = cityDto.currentDto.cloud,
        param_feelsLikeCelsius = cityDto.currentDto.feelsLikeCelsius,
        param_visibilityKm = cityDto.currentDto.visibilityKm,
        param_uvIndex = cityDto.currentDto.uvIndex,
        param_gustKph = cityDto.currentDto.gustKph,
    )

    fun mapperCityDtoToLocationDb(
        id: Int,
        cityDto: CityDto,
        position: String? = "",
        positionId: Int,
        timeUpdate: String
    ) = LocationDb(
        id = id,
        positionId = positionId,
        name = cityDto.locationDto.name,
        last_updated_epoch = cityDto.currentDto.lastUpdatedEpoch,
        last_updated = timeUpdate,
        temp_c = cityDto.currentDto.temperatureCelsius,
        localtime = Format.formatTimeLocation(cityDto.locationDto.localtime),
        region = cityDto.locationDto.region,
        country = cityDto.locationDto.country,
        position =  "${cityDto.locationDto.lat},${cityDto.locationDto.lon}",//position ?:
        tz_id = cityDto.locationDto.tz_id,
        day_maxtempC = cityDto.forecast.days[0].day.maxtempC,
        day_mintempC = cityDto.forecast.days[0].day.mintempC,
        condition_text = cityDto.currentDto.conditionDto.text,
        condition_icon = HTTPS_TEG + cityDto.currentDto.conditionDto.icon,
        condition_code = cityDto.currentDto.conditionDto.code,
    )

    fun mapperCityDtoToForecastDaysDb(cityDto: CityDto, positionId: Int): ForecastDaysDb {
        val forecastDays = cityDto.forecast.days
        val json = Gson().toJson(forecastDays)
        return ForecastDaysDb(
            id = positionId,
            nameCity = cityDto.locationDto.name,
            timeLocation = cityDto.locationDto.localtime.split(" ")[1],
            json = json
        )
    }

    private fun mapperForecastDaysDbToEntityForecastDays(forecastDaysDb: ForecastDaysDb?): List<ForecastDay> {
        return forecastDaysDb?.let {
            val type = object : TypeToken<List<ForecastDayDto>>() {}.type
            val dbModel = Gson().fromJson<List<ForecastDayDto>>(it.json, type)
            dbModel.map { dto -> mapperForecastDaysJSONToEntityForecastDays(dto) }
        } ?: emptyList()
    }

    fun mapperForecastCityDbToEntityForecastCityDays(forecastDaysDb: ForecastDaysDb) =
        ForecastDayCity(
            nameCity = forecastDaysDb.nameCity,
            timeLocation = forecastDaysDb.timeLocation,
            forecastDays = mapperForecastDaysDbToEntityForecastDays(forecastDaysDb),
        )


    private fun mapperForecastDaysJSONToEntityForecastDays(dto: ForecastDayDto) =
        ForecastDay(
            date = Format.formatDate(dto.dateEpoch),
            dateEpoch = dto.dateEpoch,
            days = Day(
                maxtempC = dto.day.maxtempC,
                mintempC = dto.day.mintempC,
                avgtempC = dto.day.avgtempC,
                maxwindKph = dto.day.maxwindKph,
                totalprecipMm = dto.day.totalprecipMm,
                totalsnowCm = dto.day.totalsnowCm,
                avgvisKm = dto.day.avgvisKm,
                avghumidity = dto.day.avghumidity,
                dailyWillItRain = dto.day.dailyWillItRain,
                dailyChanceOfRain = dto.day.dailyChanceOfRain,
                dailyWillItSnow = dto.day.dailyWillItSnow,
                dailyChanceOfSnow = dto.day.dailyChanceOfSnow,
                condition = Condition(
                    text = dto.day.condition.text,
                    icon = HTTPS_TEG + dto.day.condition.icon,
                    code = dto.day.condition.code,
                ),
            ),
            astro = Astro(
                sunrise = Format.convertTo24HourFormat(dto.astro.sunrise),
                sunset = Format.convertTo24HourFormat(dto.astro.sunset),
                moonrise = dto.astro.moonrise,
                moonset = dto.astro.moonset,
                moonPhase = dto.astro.moonPhase,
                moonIllumination = dto.astro.moonIllumination,
                isMoonUp = dto.astro.isMoonUp,
                isSunUp = dto.astro.isSunUp,
            ),
            forecastHour = dto.hour.map { mapperHourDtoToEntity(it) },
        )

    private fun mapperHourDtoToEntity(dto: HourDto) = ForecastHour(
        time = dto.time,
        temp_c = dto.tempC.toInt(),
        is_day = dto.isDay,
        condition = Condition(
            text = dto.condition.text,
            icon = HTTPS_TEG + dto.condition.icon,
            code = dto.condition.code
        ),
        feelslike_c = dto.feelslikeC.toInt(),
    )

    fun mapperLocationDbToEntityLocation(locationDb: LocationDb?) = Location(
        positionId = locationDb?.positionId ?: 0,
        name = locationDb?.name ?: "",
        region = locationDb?.region ?: "",
        country = locationDb?.country ?: "",
        temp_c = locationDb?.temp_c ?: 0.0,
        localtime = locationDb?.localtime ?: "",
        day_maxtempC = locationDb?.day_maxtempC ?: 0.0,
        day_mintempC = locationDb?.day_mintempC ?: 0.0,
        condition_text = locationDb?.condition_text ?: "",
        condition_icon = locationDb?.condition_icon ?: "",
        condition_code = locationDb?.condition_code ?: 0,
        position = locationDb?.position ?: ""
    )

    fun mapperCurrentDbToEntityCurrent(db: CurrentDb, context: Context) =
        Current(
            id = db.id,
            nameCity = db.nameCity,
            date = db.date,
            last_updated_epoch = db.last_updated_epoch,
            last_updated = db.last_updated,
            temp_c = db.temp_c,
            is_day = db.is_day,
            currentDay = Day(
                maxtempC = db.day_maxtempC,
                mintempC = db.day_mintempC,
                avgtempC = db.day_avgtempC,
                maxwindKph = db.day_maxwindKph,
                totalprecipMm = db.day_totalprecipMm,
                totalsnowCm = db.day_totalsnowCm,
                avgvisKm = db.day_avgvisKm,
                avghumidity = db.day_avghumidity,
                dailyWillItRain = db.day_dailyWillItRain,
                dailyChanceOfRain = db.day_dailyChanceOfRain,
                dailyWillItSnow = db.day_dailyWillItSnow,
                dailyChanceOfSnow = db.day_dailyChanceOfSnow,
                condition = Condition(
                    text = db.condition_text,
                    icon = db.condition_icon,
                    code = db.condition_code,
                )
            ),
            astro = Astro(
                sunrise = db.astro_sunrise,
                sunset = db.astro_sunset,
                moonrise = db.astro_moonrise,
                moonset = db.astro_moonset,
                moonPhase = db.astro_moonPhase,
                moonIllumination = db.astro_moonIllumination,
                isMoonUp = db.astro_isMoonUp,
                isSunUp = db.astro_isSunUp,
            ),
            condition = Condition(
                text = db.condition_text,
                icon = db.condition_icon,
                code = db.condition_code,
            ),
            weatherPrecipitation = getWeatherParameter(
                param_windKph = db.param_windKph.toInt(),
                param_pressureMb = db.param_pressureMb.toInt(),
                param_precipitationMm = db.param_precipitationMm.toInt(),
                param_humidity = db.param_humidity,
                param_cloud = db.param_cloud,
                param_feelsLikeCelsius = db.param_feelsLikeCelsius.toInt(),
                param_visibilityKm = db.param_visibilityKm.toInt(),
                param_uvIndex = db.param_uvIndex.toInt(),
                param_gustKph = db.param_gustKph.toInt(),
                context = context
            ),
        )


    private fun getWeatherParameter(
        param_windKph: Int,
        param_pressureMb: Int,
        param_precipitationMm: Int,
        param_humidity: Int,
        param_cloud: Int,
        param_feelsLikeCelsius: Int,
        param_visibilityKm: Int,
        param_uvIndex: Int,
        param_gustKph: Int,
        context: Context
    ): List<WeatherPrecipitation> {


        val windKph = WeatherPrecipitation(
            value = param_windKph,
            name = "Wind",
            unit = WeatherPrecipitation.VALUE_KM_H,
            maxValue = 50,
            normalValue = 177 / 2,
            minValue = 0,
            valueString = param_windKph.toString()
        )
        windKph.color = getColor(windKph, context)
        windKph.valuePercent = calculatePercentage(windKph)

        val pressureIn = WeatherPrecipitation(
            value = param_pressureMb,
            name = "Pressure", unit = WeatherPrecipitation.VALUE_MM_HG,
            maxValue = 1065, normalValue = 1035, minValue = 960,
            valueString = param_pressureMb.toString()

        )
        pressureIn.color = getColor(pressureIn, context)
        pressureIn.valuePercent = calculatePercentage(pressureIn)

        val precipitation = WeatherPrecipitation(
            value = param_precipitationMm,
            name = "Precipitation", unit = WeatherPrecipitation.VALUE_MBAR,
            maxValue = 10, minValue = 0, normalValue = 6,
            valueString = param_precipitationMm.toString()
        )
        precipitation.color = getColor(precipitation, context)
        precipitation.valuePercent = calculatePercentage(precipitation)

        val humidity = WeatherPrecipitation(
            value = param_humidity,
            name = "Humidity", unit = WeatherPrecipitation.VALUE_PERCENT,
            maxValue = 100, minValue = 0, normalValue = 50,
            valueString = "%${param_humidity.toDouble()}",
            valuePercent = param_humidity

        )
        humidity.color = getColor(humidity, context)

        val cloud = WeatherPrecipitation(
            value = param_cloud,
            name = "Cloud",
            unit = WeatherPrecipitation.VALUE_PERCENT,
            maxValue = 100,
            minValue = 0,
            normalValue = 50,
            valueString = "%${param_cloud.toDouble()}",
            valuePercent = param_cloud
        )
        cloud.color = getColor(cloud, context)

        val feelsLikeCelsius = WeatherPrecipitation(
            value = param_feelsLikeCelsius,
            name = "Feels",
            unit = WeatherPrecipitation.VALUE_DEGREE,
            maxValue = 50,
            minValue = -50,
            normalValue = 0,
            valueString = param_feelsLikeCelsius
                .toString() + WeatherPrecipitation.VALUE_DEGREE
        )
        feelsLikeCelsius.color = getColorFeels(feelsLikeCelsius, context)
        feelsLikeCelsius.valuePercent = feelsPercentage(feelsLikeCelsius)

        val visibilityKm = WeatherPrecipitation(
            value = param_visibilityKm.toInt(),
            name = "Visibility", unit = WeatherPrecipitation.VALUE_KM_H,
            maxValue = 45, minValue = 0, normalValue = 25,
            valueString = param_visibilityKm.toString()
        )
        visibilityKm.color = getColor(visibilityKm, context)
        visibilityKm.valuePercent = calculatePercentage(visibilityKm)

        val uvIndex = WeatherPrecipitation(
            value = param_uvIndex,
            name = "UV Index", unit = WeatherPrecipitation.NOT_VALUE,
            maxValue = 11, minValue = 0, normalValue = 5,
            valueString = param_uvIndex.toString(),

            )
        uvIndex.color = getColor(uvIndex, context)
        uvIndex.valuePercent = calculatePercentage(uvIndex)

        val gustKph = WeatherPrecipitation(
            value = param_gustKph,
            name = "Gust", unit = WeatherPrecipitation.VALUE_KM_H,
            maxValue = 0, minValue = 0, normalValue = 0,
            valueString = param_gustKph.toString()

        )
        return mutableListOf<WeatherPrecipitation>(
            feelsLikeCelsius, windKph, precipitation, visibilityKm, humidity, cloud,
            pressureIn, uvIndex
        )
    }

    fun feelsPercentage(weather: WeatherPrecipitation): Int {
        return weather.value + weather.maxValue
    }

    fun calculatePercentage(weather: WeatherPrecipitation): Int {
        with(weather) {
            require(minValue <= maxValue) { "minValue должно быть меньше или равно maxValue" }
            require(value in minValue..maxValue) { "value должно быть в пределах от minValue до maxValue" }

            val range = maxValue - minValue
            val adjustedValue = value - minValue
            val percentage = if (range != 0) {
                ((adjustedValue.toDouble() / range.toDouble()) * 100).toInt()
            } else {
                0 // В случае, если range равен 0, возвращаем 0, чтобы избежать деления на 0.
            }
            Log.d("Mapper", "calculatePercentage: $percentage")
            return percentage
        }
    }


    fun getColorFeels(weather: WeatherPrecipitation, context: Context): Int {
        val temperature = weather.value
        return when {
            temperature < 0 -> ContextCompat.getColor(context, R.color.precipitation_cold)
            temperature in 0..20 -> ContextCompat.getColor(context, R.color.precipitation_lite)
            temperature in 21..30 -> ContextCompat.getColor(context, R.color.precipitation_normal)
            else -> ContextCompat.getColor(context, R.color.precipitation_hard)
        }
    }

    fun getColor(weather: WeatherPrecipitation, context: Context): Int {
        val value = weather.value
        return when {
            value < weather.normalValue / 1.5 -> ContextCompat.getColor(
                context,
                R.color.precipitation_lite
            )

            value < weather.normalValue -> ContextCompat.getColor(
                context,
                R.color.precipitation_normal
            )

            value > weather.normalValue -> ContextCompat.getColor(
                context,
                R.color.precipitation_hard
            )

            else -> 0
        }
    }

    fun mapperSearchCityDtoToEntitySearchCity(searchCityDto: SearchCityDto) = SearchCity(
        id = searchCityDto.id,
        name = searchCityDto.name,
        region = searchCityDto.region,
        country = searchCityDto.country,
        lat = searchCityDto.lat,
        lon = searchCityDto.lon,
    )

    fun mapperCityDtoToEntityCity(dto: CityDto, context: Context) = City(
        location = mapperCityDtoToLocationEntity(dto),
        current = mapperCurrentDtoToEntityCurrent(dto, context),
        forecastDays = dto.forecast.days.map { mapperForecastDaysDtoToEntityForecastDays(it) }
    )

    private fun mapperCityDtoToLocationEntity(cityDto: CityDto) = Location(
        positionId = EMPTY_ID,
        name = cityDto.locationDto.name,
        region = cityDto.locationDto.region,
        country = cityDto.locationDto.country,
        temp_c = cityDto.currentDto.temperatureCelsius,
        localtime = Format.formatTimeLocation(cityDto.locationDto.localtime),
        position = "${cityDto.locationDto.lat},${cityDto.locationDto.lon}",
        day_maxtempC = cityDto.forecast.days[0].day.maxtempC,
        day_mintempC = cityDto.forecast.days[0].day.mintempC,
        condition_text = cityDto.currentDto.conditionDto.text,
        condition_icon = HTTPS_TEG + cityDto.currentDto.conditionDto.icon,
        condition_code = cityDto.currentDto.conditionDto.code,
    )

    fun mapperCurrentDtoToEntityCurrent(dto: CityDto, context: Context) =
        Current(
            id = EMPTY_ID,
            nameCity = dto.locationDto.name,
            date = Format.formatDate(dto.locationDto.localtime_epoch),
            last_updated_epoch = dto.locationDto.localtime_epoch,
            last_updated = dto.locationDto.localtime,
            temp_c = dto.currentDto.temperatureCelsius,
            is_day = dto.currentDto.isDay,
            currentDay = Day(
                maxtempC = dto.forecast.days[0].day.maxtempC,
                mintempC = dto.forecast.days[0].day.mintempC,
                avgtempC = dto.forecast.days[0].day.avgtempC,
                maxwindKph = dto.forecast.days[0].day.maxwindKph,
                totalprecipMm = dto.forecast.days[0].day.totalprecipMm,
                totalsnowCm = dto.forecast.days[0].day.totalsnowCm,
                avgvisKm = dto.forecast.days[0].day.avgvisKm,
                avghumidity = dto.forecast.days[0].day.avghumidity,
                dailyWillItRain = dto.forecast.days[0].day.dailyWillItRain,
                dailyChanceOfRain = dto.forecast.days[0].day.dailyChanceOfRain,
                dailyWillItSnow = dto.forecast.days[0].day.dailyWillItSnow,
                dailyChanceOfSnow = dto.forecast.days[0].day.dailyChanceOfSnow,
                condition = Condition(
                    text = dto.currentDto.conditionDto.text,
                    icon = HTTPS_TEG + dto.currentDto.conditionDto.icon,
                    code = dto.currentDto.conditionDto.code,
                )
            ),
            astro = Astro(
                sunrise = dto.forecast.days[0].astro.sunrise,
                sunset = dto.forecast.days[0].astro.sunset,
                moonrise = dto.forecast.days[0].astro.moonrise,
                moonset = dto.forecast.days[0].astro.moonset,
                moonPhase = dto.forecast.days[0].astro.moonPhase,
                moonIllumination = dto.forecast.days[0].astro.moonIllumination,
                isMoonUp = dto.forecast.days[0].astro.isMoonUp,
                isSunUp = dto.forecast.days[0].astro.isSunUp,
            ),
            condition = Condition(
                text = dto.currentDto.conditionDto.text,
                icon = HTTPS_TEG + dto.currentDto.conditionDto.icon,
                code = dto.currentDto.conditionDto.code,
            ),
            weatherPrecipitation = getWeatherParameter(
                param_windKph = dto.currentDto.windKph.toInt(),
                param_pressureMb = dto.currentDto.pressureMb.toInt(),
                param_precipitationMm = dto.currentDto.precipitationMm.toInt(),
                param_humidity = dto.currentDto.humidity,
                param_cloud = dto.currentDto.cloud,
                param_feelsLikeCelsius = dto.currentDto.feelsLikeCelsius.toInt(),
                param_visibilityKm = dto.currentDto.visibilityKm.toInt(),
                param_uvIndex = dto.currentDto.uvIndex.toInt(),
                param_gustKph = dto.currentDto.gustKph.toInt(),
                context = context
            ),
        )

    private fun mapperForecastDaysDtoToEntityForecastDays(dto: ForecastDayDto) =
        ForecastDay(
            date = Format.formatDate(dto.dateEpoch),
            dateEpoch = dto.dateEpoch,
            days = Day(
                maxtempC = dto.day.maxtempC,
                mintempC = dto.day.mintempC,
                avgtempC = dto.day.avgtempC,
                maxwindKph = dto.day.maxwindKph,
                totalprecipMm = dto.day.totalprecipMm,
                totalsnowCm = dto.day.totalsnowCm,
                avgvisKm = dto.day.avgvisKm,
                avghumidity = dto.day.avghumidity,
                dailyWillItRain = dto.day.dailyWillItRain,
                dailyChanceOfRain = dto.day.dailyChanceOfRain,
                dailyWillItSnow = dto.day.dailyWillItSnow,
                dailyChanceOfSnow = dto.day.dailyChanceOfSnow,
                condition = Condition(
                    text = dto.day.condition.text,
                    icon = HTTPS_TEG + dto.day.condition.icon,
                    code = dto.day.condition.code,
                ),
            ),
            astro = Astro(
                sunrise = Format.convertTo24HourFormat(dto.astro.sunrise),
                sunset = Format.convertTo24HourFormat(dto.astro.sunset),
                moonrise = dto.astro.moonrise,
                moonset = dto.astro.moonset,
                moonPhase = dto.astro.moonPhase,
                moonIllumination = dto.astro.moonIllumination,
                isMoonUp = dto.astro.isMoonUp,
                isSunUp = dto.astro.isSunUp,
            ),
            forecastHour = dto.hour.map { mapperHourDtoToEntity(it) },
        )


    companion object {
        const val HTTPS_TEG = "https:"
        const val EMPTY_ID = 0
    }
}