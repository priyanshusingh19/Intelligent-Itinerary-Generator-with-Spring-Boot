# PS Trip Planner - Spring Boot Backend

A Spring Boot REST API backend for intelligent trip planning powered by AI. This application generates personalized travel itineraries based on user preferences, budget, duration, and interests.

## Features

- **AI-Powered Itinerary Generation**: Uses AI model for intelligent trip planning
- **User Preference Analysis**: Considers travel style, budget, duration, interests, and constraints
- **RESTful API**: Clean and intuitive REST endpoints for trip planning
- **Real-time Processing**: Fast response times with AI-powered processing
- **Extensible Architecture**: Easy to add new features and integrations

## Technology Stack

- **Framework**: Spring Boot 3.x
- **Language**: Java 17+
- **Build Tool**: Maven
- **AI Integration**: Google Generative AI SDK (Gemini)
- **API Client**: Direct integration with Gemini API
- **Database**: (Optional - for storing user preferences and trip history)
- **Documentation**: Springdoc OpenAPI (Swagger UI)

## Prerequisites

- Java 17 or higher
- Maven 3.8+
- Gemini API Key ([Get one here](https://aistudio.google.com/app/apikey))

## Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd pstripplanner
   ```

2. **Set up environment variables**
   Create a `.env` file in the project root:
   ```env
   GEMINI_API_KEY=your_gemini_api_key_here
   AI_MODEL=gemini-2.0-flash-exp
   ```
   The application will automatically load these variables.

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## API Endpoints

### Generate Trip Itinerary
**POST** `/api/trips/generate`

Request body:
```json
{
  "startCity": "London, UK",
  "destination": "Paris, France",
  "duration": 5,
  "budget": "medium",
  "interests": ["museums", "food", "architecture"],
  "travelStyle": "cultural",
  "startDate": "2025-06-01",
  "numberOfTravelers": 2,
  "additionalPreferences": "Prefer walking tours and local restaurants"
}
```

Response:
```json
{
  "tripId": "trip_123456",
  "destination": "Paris, France",
  "itinerary": [
    {
      "day": 1,
      "activities": [...],
      "recommendations": [...]
    }
  ],
  "estimatedBudget": 2500,
  "generatedAt": "2025-01-15T10:30:00Z"
}
```

## Project Structure

```
pstripplanner/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/tripplanner/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── model/
│   │   │       ├── config/
│   │   │       └── TripPlannerApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-dev.properties
│   └── test/
├── pom.xml
└── README.md
```

## Configuration

### application.properties
```properties
spring.application.name=pstripplanner
server.port=8080

# AI API Configuration (loaded from .env)
ai.api.key=${GEMINI_API_KEY}
ai.model=${AI_MODEL:gemini-2.0-flash-exp}

# Logging
logging.level.root=INFO
logging.level.com.pstripplanner=DEBUG
```

## Development

### Running Tests
```bash
mvn test
```

### Building Docker Image
```bash
mvn clean package
docker build -t pstripplanner:latest .
docker run -p 8080:8080 --env-file .env pstripplanner:latest
```

## API Documentation

Once the application is running, access the Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

## Contributing

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -am 'Add new feature'`
3. Push to branch: `git push origin feature/your-feature`
4. Submit a pull request

## Security

- **API Key Management**: Store API keys in `.env` file, never commit it to version control
- **Input Validation**: All user inputs are validated before sending to the AI service
- **Rate Limiting**: Implement rate limiting to prevent API abuse
- **CORS**: Configure CORS appropriately for your frontend
- **.env File**: Add `.env` to `.gitignore` to prevent accidental exposure of credentials

## Troubleshooting

### Issue: "API Key not found"
- Ensure `.env` file exists in the project root
- Verify `GEMINI_API_KEY` is set in the `.env` file
- Check that the `.env` file is not gitignored

### Issue: "Connection timeout"
- Verify internet connectivity
- Check that `AI_MODEL` is correctly specified in `.env`
- Verify API key is valid for the configured model

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For issues and questions, please open an issue on the repository or contact the development team.

## References

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring REST Documentation](https://spring.io/guides/gs/rest-service/)
- [Google Gemini API Documentation](https://ai.google.dev/gemini-api/docs)
- [Get Gemini API Key](https://aistudio.google.com/app/apikey)
