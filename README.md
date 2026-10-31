# Spring_AI_MARK1

A Spring Boot + Spring AI project with a few AI features, and a plain HTML/CSS/JS frontend served from Spring Boot's `static` folder.

## What it does

- **Chat**: ask a question and get an answer from OpenAI or a local Ollama model
- **Voice assistant**: send a question, get the answer back as spoken audio (WAV), with the answer text in the `X-Answer-Text` response header
- **Image describe**: upload an image, ask a question about it, get a text answer

## Endpoints

| Endpoint | What it does |
|----------|--------------|
| `/ai/openAi/generate` | Chat using OpenAI |
| `/ai/ollama/generate` | Chat using Ollama (local) |
| `POST /api/voice/ask` | JSON body `{ "question": "...", "voice": "..." }`, returns `audio/wav` |
| `POST /api/image/describe` | Multipart form with `file` (image) and `question`, returns text |

## Setup

### Requirements

- Java 17+
- Maven (or just use the included `mvnw`)
- OpenAI API key
- [Ollama](https://ollama.com) installed, with a model pulled (e.g. `ollama pull llama3`)

### Steps

1. Clone the repo
   ```bash
   git clone https://github.com/DhruvGautam099/SpringAIMK1.git
   cd SpringAIMK1
   ```

2. Create a `.env` file in the project root and add your key
   ```env
   OPENAI_API_KEY=your_key_here
   ```

3. Check `src/main/resources/application.properties` and set your keys/models
   ```properties
   spring.ai.openai.api-key=${OPENAI_API_KEY}
   spring.ai.ollama.base-url=http://localhost:11434
   ```

4. Start Ollama (only needed for the Ollama chat endpoint)
   ```bash
   ollama serve
   ```

5. Run the app
   ```bash
   ./mvnw spring-boot:run
   ```
   On Windows: `mvnw.cmd spring-boot:run`

6. Open **http://localhost:8080** in your browser

> Don't push your `.env` file. Add `.env` to `.gitignore`.