package com.example.weatherforecastapp.data.mapper

import com.example.testapi.network.model.forecastdaysModels.ForecastDayDto
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
import com.example.weatherforecastapp.domain.models.Location
import com.example.weatherforecastapp.domain.models.SearchCity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Mapper {


    fun mapperCityDtoToCurrentDb(id: Int, cityDto: CityDto) = CurrentDb(
        id = id,
        nameCity = cityDto.locationDto.name,
        date = cityDto.forecast.days[0].date,
        last_updated_epoch = cityDto.currentDto.last_updated_epoch,
        last_updated = cityDto.currentDto.last_updated,
        temp_c = cityDto.currentDto.temp_c,
        is_day = cityDto.currentDto.is_day,
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
    )

    fun mapperCityDtoToLocationDb(
        id: Int,
        cityDto: CityDto,
        position: String? = "",
    ) = LocationDb(
        id = id,
        name = cityDto.locationDto.name,
        last_updated_epoch = cityDto.currentDto.last_updated_epoch,
        temp_c = cityDto.currentDto.temp_c,
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

    fun mapperCityDtoToForecastDaysDb(id: Int, cityDto: CityDto): ForecastDaysDb {
        val forecastDays = cityDto.forecast.days
        val json = Gson().toJson(forecastDays)
        return ForecastDaysDb(
            id = id,
            nameCity = cityDto.locationDto.name,
            json = json
        )
    }

    fun mapperForecastDaysDbToEntityForecastDays(forecastDaysDb: ForecastDaysDb): List<ForecastDay> {
        val type = object : TypeToken<List<ForecastDayDto>>() {}.type
        return Gson().fromJson(forecastDaysDb.json, type)
    }

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
    )


    fun mapperSearchCityDtoToEntitySearchCity(searchCityDto: SearchCityDto) = SearchCity(
        id = searchCityDto.id,
        name = searchCityDto.name,
        region = searchCityDto.region,
        country = searchCityDto.country,
    )


    fun mapperCityDbToEntityCity(cityDb: CityDb) = City(
        id = cityDb.id,
        location = mapperLocationDbToEntityLocation(cityDb.locationDb),
        currentDto = mapperCurrentDbToEntityCurrent(cityDb.currentDb),
        forecastDay = mapperForecastDaysDbToEntityForecastDays(cityDb.forecastDaysDb)
    )

}