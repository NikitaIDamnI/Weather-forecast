package com.example.weatherforecastapp.data.mapper

import com.example.testapi.network.model.forecastdaysModels.ForecastDayDto
import com.example.testapi.network.model.forecastdaysModels.HourDto
import com.example.testapi.network.model.searchCityModels.SearchCityDto
import com.example.weatherforecastapp.data.database.models.CityDb
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
import com.example.weatherforecastapp.domain.models.ForecastHour
import com.example.weatherforecastapp.domain.models.Location
import com.example.weatherforecastapp.domain.models.SearchCity
import com.example.weatherforecastapp.domain.models.WeatherParameter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class Mapper {


    fun mapperCityDtoToCurrentDb(cityDto: CityDto, positionId: Int) = CurrentDb(
        id = positionId,
        nameCity = cityDto.locationDto.name,
        date = formatTime(cityDto.currentDto.lastUpdatedEpoch),
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
        condition_text = cityDto.forecast.days[0].day.condition.text,
        condition_icon = cityDto.forecast.days[0].day.condition.icon,
        condition_code = cityDto.forecast.days[0].day.condition.code,
        param_windKph = cityDto.currentDto.windKph,
        param_windDegree = cityDto.currentDto.windDegree,
        param_pressureIn = cityDto.currentDto.pressureIn,
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
        positionId: Int
    ) = LocationDb(
        id = id,
        positionId = positionId,
        name = cityDto.locationDto.name,
        last_updated_epoch = cityDto.currentDto.lastUpdatedEpoch,
        temp_c = cityDto.currentDto.temperatureCelsius,
        localtime = cityDto.locationDto.localtime,
        region = cityDto.locationDto.region,
        country = cityDto.locationDto.country,
        position = position ?: "${cityDto.locationDto.lat},${cityDto.locationDto.lon}",
        tz_id = cityDto.locationDto.tz_id,
        day_maxtempC = cityDto.forecast.days[0].day.maxtempC,
        day_mintempC = cityDto.forecast.days[0].day.mintempC,
        condition_text = cityDto.forecast.days[0].day.condition.text,
        condition_icon = cityDto.forecast.days[0].day.condition.icon,
        condition_code = cityDto.forecast.days[0].day.condition.code,
    )

    fun mapperCityDtoToForecastDaysDb(cityDto: CityDto, positionId: Int): ForecastDaysDb {
        val forecastDays = cityDto.forecast.days
        val json = Gson().toJson(forecastDays)
        return ForecastDaysDb(
            id = positionId,
            nameCity = cityDto.locationDto.name,
            json = json
        )
    }

    fun mapperForecastDaysDbToEntityForecastDays(forecastDaysDb: ForecastDaysDb): List<ForecastDay> {
        val type = object : TypeToken<List<ForecastDayDto>>() {}.type
        val dbModel = Gson().fromJson<List<ForecastDayDto>>(forecastDaysDb.json, type)
        return dbModel.map { mapperForecastDaysDtoToEntityForecastDays(it, forecastDaysDb) }
    }

    private fun mapperForecastDaysDtoToEntityForecastDays(dto: ForecastDayDto, db: ForecastDaysDb) =
        ForecastDay(
            nameCity = db.nameCity,
            date = dto.date,
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
                    icon = dto.day.condition.icon,
                    code = dto.day.condition.code,
                ),
            ),
            astro = Astro(
                sunrise = dto.astro.sunrise,
                sunset = dto.astro.sunset,
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
        temp_c = dto.tempC,
        is_day = dto.isDay,
        condition = dto.condition,
        feelslike_c = dto.feelslikeC,
    )

    fun mapperLocationDbToEntityLocation(locationDb: LocationDb) = Location(
        name = locationDb.name,
        temp_c = locationDb.temp_c,
        localtime = locationDb.localtime,
        day_maxtempC = locationDb.day_maxtempC,
        day_mintempC = locationDb.day_mintempC,
        condition_text = locationDb.condition_text,
        condition_icon = locationDb.condition_icon,
        condition_code = locationDb.condition_code,
        position = locationDb.position

    )

    fun mapperCurrentDbToEntityCurrent(currentDb: CurrentDb) = Current(
        id = currentDb.id,
        nameCity = currentDb.nameCity,
        date = currentDb.date,
        last_updated_epoch = currentDb.last_updated_epoch,
        last_updated = currentDb.last_updated,
        temp_c = currentDb.temp_c,
        is_day = currentDb.is_day,
        currentDay = Day(
            maxtempC = currentDb.day_maxtempC,
            mintempC = currentDb.day_mintempC,
            avgtempC = currentDb.day_avgtempC,
            maxwindKph = currentDb.day_maxwindKph,
            totalprecipMm = currentDb.day_totalprecipMm,
            totalsnowCm = currentDb.day_totalsnowCm,
            avgvisKm = currentDb.day_avgvisKm,
            avghumidity = currentDb.day_avghumidity,
            dailyWillItRain = currentDb.day_dailyWillItRain,
            dailyChanceOfRain = currentDb.day_dailyChanceOfRain,
            dailyWillItSnow = currentDb.day_dailyWillItSnow,
            dailyChanceOfSnow = currentDb.day_dailyChanceOfSnow,
            condition = Condition(
                text = currentDb.condition_text,
                icon = currentDb.condition_icon,
                code = currentDb.condition_code,
            )
        ),
        astro = Astro(
            sunrise = currentDb.astro_sunrise,
            sunset = currentDb.astro_sunset,
            moonrise = currentDb.astro_moonrise,
            moonset = currentDb.astro_moonset,
            moonPhase = currentDb.astro_moonPhase,
            moonIllumination = currentDb.astro_moonIllumination,
            isMoonUp = currentDb.astro_isMoonUp,
            isSunUp = currentDb.astro_isSunUp,
        ),
        condition = Condition(
            text = currentDb.condition_text,
            icon = currentDb.condition_icon,
            code = currentDb.condition_code,
        ),
        weatherParameter = getWeatherParameter(currentDb),
    )


    private fun getWeatherParameter(currentDb: CurrentDb): List<WeatherParameter> {
        val windKph = WeatherParameter(
            value = currentDb.param_windKph, name = "Wind", unit = WeatherParameter.VALUE_KM_H,
        )
        val windDegree = WeatherParameter(
            value = currentDb.param_windDegree.toDouble(),
            name = "Wind Degree", unit = WeatherParameter.VALUE_DEGREE,
        )
        val pressureIn = WeatherParameter(
            value = currentDb.param_pressureIn,
            name = "Pressure", unit = WeatherParameter.VALUE_MM_HG
        )
        val precipitation = WeatherParameter(
            value = currentDb.param_precipitationMm,
            name = "Precipitation", unit = WeatherParameter.VALUE_MM
        )
        val humidity = WeatherParameter(
            value = currentDb.param_humidity.toDouble(),
            name = "Humidity", unit = WeatherParameter.VALUE_PERCENT
        )
        val cloud = WeatherParameter(
            value = currentDb.param_cloud.toDouble(),
            name = "Cloud", unit = WeatherParameter.VALUE_PERCENT
        )
        val feelsLikeCelsius = WeatherParameter(
            value = currentDb.param_feelsLikeCelsius,
            name = "Feels Like", unit = WeatherParameter.VALUE_DEGREE
        )
        val visibilityKm = WeatherParameter(
            value = currentDb.param_visibilityKm,
            name = "Visibility", unit = WeatherParameter.VALUE_KM_H
        )
        val uvIndex = WeatherParameter(
            value = currentDb.param_uvIndex,
            name = "UV Index", unit = WeatherParameter.NOT_VALUE
        )
        val gustKph = WeatherParameter(
            value = currentDb.param_gustKph,
            name = "Gust", unit = WeatherParameter.VALUE_KM_H
        )
        return mutableListOf<WeatherParameter>(
            feelsLikeCelsius, windKph, windDegree, visibilityKm, humidity, cloud, precipitation,
            pressureIn, gustKph, uvIndex
        )
    }


    fun mapperSearchCityDtoToEntitySearchCity(searchCityDto: SearchCityDto) = SearchCity(
        id = searchCityDto.id,
        name = searchCityDto.name,
        region = searchCityDto.region,
        country = searchCityDto.country,
        lat = searchCityDto.lat,
        lon = searchCityDto.lon,
    )


    fun mapperCityDbToEntityCity(cityDb: CityDb) = City(
        id = cityDb.id,
        location = mapperLocationDbToEntityLocation(cityDb.locationDb),
        current = mapperCurrentDbToEntityCurrent(cityDb.currentDb),
        forecastDays = mapperForecastDaysDbToEntityForecastDays(cityDb.forecastDaysDb)
    )

    private fun formatTime(epochTime: Long): String {
        val dateFormat = SimpleDateFormat("EEEE, d MMM yyyy", Locale.ENGLISH)
        val date = Date(epochTime * 1000)
        return dateFormat.format(date)
    }
}