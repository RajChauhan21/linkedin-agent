# LinkedIn Agent - Automated LinkedIn Content Generator

# 📋Overview
LinkedIn Agent is an automated Spring Boot application that generates and posts engaging technical content to LinkedIn daily. The application fetches the latest technology news, creates AI-generated images, and publishes professional posts with relevant hashtags.

# 🎯 Key Features
Automated Daily Posts: Scheduled to run every day at 9:00 AM

Tech News Integration: Fetches latest technology news from external news API

AI Image Generation: Creates animated images using Cloudflare's AI image generation API

LinkedIn Integration: Automatically posts to LinkedIn using official LinkedIn Developers API

Smart Content Selection: Intelligently selects the most relevant tech news for posting

# 🏗️ Architecture
┌─────────────┐    ┌──────────────┐    ┌─────────────────┐
│   News API  │───▶│  News Fetcher│───▶│ Content Selector│
└─────────────┘    └──────────────┘    └────────┬────────┘
                                                │
┌─────────────┐    ┌──────────────┐             │
│Cloudflare AI│◀───│Image Generator│◀────────────┘
│    Image    │    │              │
└─────────────┘    └──────────────┘
                           │
                    ┌──────▼──────┐
                    │   LinkedIn  │
                    │    Poster   │
                    └──────┬──────┘
                           │
                    ┌──────▼──────┐
                    │  LinkedIn   │
                    │    API      │
                    └─────────────┘



 # 🛠️ Technology Stack
Framework: Spring Boot 3.x

Language: Java 17+

Scheduling: Spring Scheduler (@Scheduled)

APIs Integrated:

News API (Technology News)

Cloudflare AI (Image Generation)

LinkedIn Developers API (Content Publishing)

Build Tool: Maven/Gradle

Version Control: Git

AI Model: groq/openai/gpt-oss-120b

# 📦 Prerequisites
Java 17 or higher

Maven 3.6+ or Gradle 7+

LinkedIn Developer Account

News API Key

Cloudflare API Key with AI access
