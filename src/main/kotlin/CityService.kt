package com.example

import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import java.sql.Connection
import java.sql.Statement


@Serializable
data class CityUpdateRequest(
    val name: String? = null,
    val country: String? = null,
    val stateProvince: String? = null,
    val population: Int? = null,
    val majorIndustries: String? = null,
    val climateType: String? = null
)

@Serializable
data class City(val name: String, val country: String, val stateProvince: String, val population: Int, val majorIndustries: String, val climateType: String)
class CityService(private val connection: Connection) {
    companion object {
        private const val CREATE_TABLE_CITIES = """
        CREATE TABLE IF NOT EXISTS CITIES (
            ID SERIAL PRIMARY KEY,
            NAME VARCHAR(255) NOT NULL,
            COUNTRY VARCHAR(100) NOT NULL,
            STATE_PROVINCE VARCHAR(100) NOT NULL,
            POPULATION INT NOT NULL,
            MAJOR_INDUSTRIES TEXT,
            CLIMATE_TYPE VARCHAR(50)
        );
    """
    

        private const val SELECT_CITY_BY_ID = "SELECT name, population FROM cities WHERE id = ?"
        private const val INSERT_CITY = "INSERT INTO cities (name, country, state_province, population, major_industries, climate_type) VALUES (?, ?, ?, ?, ?, ?)"
        private const val UPDATE_CITY = "UPDATE cities SET name = ?, country = ?, state_province = ?, population = ?, major_industries = ?, climate_type = ? WHERE id = ?"
        private const val DELETE_CITY = "DELETE FROM cities WHERE id = ?"
        private const val GET_ALL_CITIES = "SELECT * FROM cities"
    }

    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_CITIES)
    }

    private var newCityId = 0

    // Create new city
    suspend fun create(city: City): Int = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT_CITY, Statement.RETURN_GENERATED_KEYS)
        statement.setString(1, city.name)
        statement.setString(2, city.country)
        statement.setString(3, city.stateProvince)
        statement.setInt(4, city.population)
        statement.setString(5, city.majorIndustries)
        statement.setString(6, city.climateType)

        statement.executeUpdate()

        val generatedKeys = statement.generatedKeys
        if (generatedKeys.next()) {
            return@withContext generatedKeys.getInt(1)
        } else {
            throw Exception("Unable to retrieve the id of the newly inserted city")
        }
    }

    suspend fun allCities() = withContext(Dispatchers.IO) {
        connection.prepareStatement(GET_ALL_CITIES).use { statement ->
            statement.executeQuery().use { resultSet ->
                val cities = mutableListOf<City>()
                while (resultSet.next()) {
                    cities.add(
                        City(
                            name = resultSet.getString("name"),
                            population = resultSet.getInt("population"),
                            country = resultSet.getString("country"),
                            stateProvince = resultSet.getString("state_province"),
                            majorIndustries = resultSet.getString("major_industries"),
                            climateType = resultSet.getString("climate_type")
                        )
                    )
                }
                return@withContext cities;
            }
        }
    }

    // Read a city
    suspend fun read(id: Int): City = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_CITY_BY_ID)
        statement.setInt(1, id)
        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            val name = resultSet.getString("name")
            val population = resultSet.getInt("population")
            val country = resultSet.getString("country")
            val stateProvince = resultSet.getString("state_province")
            val majorIndustries = resultSet.getString("major_industries")
            val climateType = resultSet.getString("climate_type")
            return@withContext City(name, country, stateProvince, population, majorIndustries, climateType)
        } else {
            throw Exception("Record not found")
        }
    }

    // Update a city
    suspend fun update(id: Int, city: City) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(UPDATE_CITY)
        statement.setString(1, city.name)
        statement.setString(2, city.country)
        statement.setString(3, city.stateProvince)
        statement.setInt(4, city.population)
        statement.setString(5, city.majorIndustries)
        statement.setString(6, city.climateType)
        statement.setInt(7, id)
        statement.executeUpdate()
    }

    suspend fun updateSingleCityParam(id: Int, updateRequest: CityUpdateRequest) = withContext(Dispatchers.IO) {
        val fields = mutableListOf<String>()
        val values = mutableListOf<Any>()
    
        updateRequest.name?.let { fields.add("name = ?"); values.add(it) }
        updateRequest.country?.let { fields.add("country = ?"); values.add(it) }
        updateRequest.stateProvince?.let { fields.add("state_province = ?"); values.add(it) }
        updateRequest.population?.let { fields.add("population = ?"); values.add(it) }
        updateRequest.majorIndustries?.let { fields.add("major_industries = ?"); values.add(it) }
        updateRequest.climateType?.let { fields.add("climate_type = ?"); values.add(it) }
    
        if (fields.isEmpty()) return@withContext 
    
        val sql = "UPDATE cities SET ${fields.joinToString(", ")} WHERE id = ?"
        values.add(id)
    
        val statement = connection.prepareStatement(sql)
        values.forEachIndexed { index, value ->
            when (value) {
                is String -> statement.setString(index + 1, value)
                is Int -> statement.setInt(index + 1, value)
            }
        }
        
        statement.setInt(values.size + 1, id)
        statement.executeUpdate()
    }
    

    // Delete a city
    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(DELETE_CITY)
        statement.setInt(1, id)
        statement.executeUpdate()
    }
}

