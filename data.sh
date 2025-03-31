curl -X POST http://localhost:8080/cities \
     -H "Content-Type: application/json" \
     -d '{
           "name": "New York",
           "country": "USA",
           "stateProvince": "New York",
           "population": 8419600,
           "majorIndustries": "Finance, Media, Technology",
           "climateType": "Temperate"
         }'


# 1. Add Los Angeles
curl -X POST http://localhost:8080/cities \
     -H "Content-Type: application/json" \
     -d '{
           "name": "Los Angeles",
           "country": "USA",
           "stateProvince": "California",
           "population": 3980400,
           "majorIndustries": "Entertainment, Aerospace, Technology",
           "climateType": "Mediterranean"
         }'

# 2. Add London
curl -X POST http://localhost:8080/cities \
     -H "Content-Type: application/json" \
     -d '{
           "name": "London",
           "country": "UK",
           "stateProvince": "England",
           "population": 8982000,
           "majorIndustries": "Finance, Tourism, Technology",
           "climateType": "Temperate"
         }'

# 3. Add Tokyo
curl -X POST http://localhost:8080/cities \
     -H "Content-Type: application/json" \
     -d '{
           "name": "Tokyo",
           "country": "Japan",
           "stateProvince": "Tokyo Metropolis",
           "population": 13929000,
           "majorIndustries": "Technology, Manufacturing, Finance",
           "climateType": "Humid Subtropical"
         }'

# 4. Add Sydney
curl -X POST http://localhost:8080/cities \
     -H "Content-Type: application/json" \
     -d '{
           "name": "Sydney",
           "country": "Australia",
           "stateProvince": "New South Wales",
           "population": 5312163,
           "majorIndustries": "Tourism, Finance, Education",
           "climateType": "Temperate"
         }'

# 5. Add Paris
curl -X POST http://localhost:8080/cities \
     -H "Content-Type: application/json" \
     -d '{
           "name": "Paris",
           "country": "France",
           "stateProvince": "Île-de-France",
           "population": 2148000,
           "majorIndustries": "Fashion, Tourism, Finance",
           "climateType": "Oceanic"
         }'

# 6. Add Berlin
curl -X POST http://localhost:8080/cities \
     -H "Content-Type: application/json" \
     -d '{
           "name": "Berlin",
           "country": "Germany",
           "stateProvince": "Berlin",
           "population": 3769000,
           "majorIndustries": "Tech, Startups, Government",
           "climateType": "Oceanic"
         }'
