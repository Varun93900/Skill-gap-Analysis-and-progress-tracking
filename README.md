# Skill Gap Analysis and Progress Tracking System

## Overview

The Skill Gap Analysis and Progress Tracking System is a Java Full Stack web application designed to help students identify the skills they need to improve for a particular job role and track their progress over time.

Users can create an account, add their existing skills with proficiency levels, select a target job role, and analyze the difference between their current skills and the skills required for that role.

The system also provides administrators with functionality to manage job roles, skills, skill categories, and the skills required for different job roles.

## Features

### User Features

- User registration and login
- JWT based authentication
- Add and manage personal skills
- Set skill proficiency levels such as Beginner, Intermediate, and Advanced
- Select a target job role
- Analyze skill gaps
- View skills required for a selected job role
- Track skill development
- View roadmap and learning content

### Admin Features

- Admin authentication
- Manage users
- Manage job roles
- Manage skills
- Manage skill categories
- Define skills required for different job roles
- Manage skill priorities
- Manage learning content and roadmaps

## Skill Gap Analysis

The system compares the skills entered by a user with the skills required for the selected job role.

Based on the comparison, the system identifies skills that are missing or need improvement. This helps users understand which areas they should focus on to move toward their target job role.

The analysis is based on predefined skills and job role requirements rather than an AI-based recommendation system.

## Progress Tracking

Users can update their skill proficiency as they improve their knowledge.

The system uses the updated skill information to show the user's progress toward the requirements of their selected job role.

## Technology Stack

Frontend:
- React.js
- JavaScript
- HTML
- CSS

Backend:
- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- Spring Security
- JWT
- REST APIs
- Maven

Database:
- MySQL

Tools:
- IntelliJ IDEA
- Visual Studio Code
- Git
- GitHub
- Postman
- Swagger

## Project Structure

```text
Skill-Gap-Analysis-and-progress-tracking
|
|-- Backend
|   |-- src
|   |   |-- main
|   |       |-- java
|   |       |   |-- com.skillgap.skillgap
|   |       |       |-- config
|   |       |       |-- controller
|   |       |       |-- dto
|   |       |       |-- entity
|   |       |       |-- exception
|   |       |       |-- repository
|   |       |       |-- security
|   |       |       |-- service
|   |       |       |-- util
|   |       |
|   |       |-- resources
|   |
|   |-- pom.xml
|   |-- mvnw
|
|-- Frontend
|   |-- public
|   |-- src
|   |-- package.json
|   |-- package-lock.json
