# 📞 Phonebook Full-Stack Application

A complete phone contact management application developed as part of the **Full Stack Open** course by the University of Helsinki.

## 🌐 Live Demo
**[View working application](https://phonebook-api-fdwy.onrender.com/)**

## 🛠️ Tech Stack

### Backend
- **Node.js** - Runtime environment
- **Express.js** - Web framework
- **MongoDB Atlas** - Cloud database
- **Mongoose** - MongoDB ODM
- **Morgan** - Logging middleware
- **CORS** - Cross-origin request handling
- **dotenv** - Environment variables

### Frontend
- **React** - User interface library
- **Axios** - HTTP client for requests
- **Vite** - Build tool

### DevOps & Tools
- **Render** - Deployment platform
- **ESLint** - Code linting and quality
- **Git** - Version control

## ✨ Features

### Core Functionality
- ✅ **Complete CRUD** - Create, read, update, and delete contacts
- ✅ **Real-time search** - Filter contacts by name
- ✅ **Robust validations** - Both frontend and backend
- ✅ **Error handling** - Clear user feedback
- ✅ **Smart updates** - Confirm before overwriting numbers

### Technical Features
- ✅ **REST API** - Semantically correct endpoints
- ✅ **Persistent database** - MongoDB Atlas in the cloud
- ✅ **Custom middleware** - Centralized error handling
- ✅ **Request logging** - Monitoring with Morgan
- ✅ **Clean code** - ESLint configured
- ✅ **Environment variables** - Secure configuration

## 🚀 Installation and Usage

### Prerequisites
- Node.js (v16 or higher)
- npm or yarn
- MongoDB Atlas account (for the database)

### Backend
```bash
# Clone the repository
git clone https://github.com/tortilicious/phonebook-api.git
cd phonebook-api

# Install dependencies
npm install

# Configure environment variables
# Create .env file with:
MONGODB_URI=your_mongodb_connection_string
PORT=3001

# Run in development
npm run dev

# Or run in production
npm start
```

### Frontend
```bash
# Clone the course repository
git clone https://github.com/tortilicious/fullstackopen_course.git
cd fullstackopen_course/part2

# Install dependencies
npm install

# Run in development
npm run dev

# Build for production
npm run build
```

## 📡 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/contacts` | Get all contacts |
| GET | `/api/contacts/:id` | Get a specific contact |
| POST | `/api/contacts` | Create a new contact |
| PUT | `/api/contacts/:id` | Update an existing contact |
| DELETE | `/api/contacts/:id` | Delete a contact |
| GET | `/api/info` | Get phonebook information |

### Example Request
```javascript
// POST /api/contacts
{
  "name": "John Doe",
  "number": "123-456789"
}
```

### Example Response
```javascript
{
  "id": "64f8a1b2c3d4e5f6a7b8c9d0",
  "name": "John Doe",
  "number": "123-456789"
}
```

## 🔧 Development Configuration

### Available Scripts
```bash
# Backend
npm run dev          # Run with nodemon
npm start           # Run in production
npm run lint        # Run ESLint
npm run lint:fix    # Auto-fix ESLint errors

# Frontend
npm run dev         # Development server
npm run build       # Build for production
npm run preview     # Preview build
```

### Environment Variables
```env
MONGODB_URI=mongodb+srv://username:password@cluster.mongodb.net/phonebook?retryWrites=true&w=majority
PORT=3001
```

## 🏗️ Architecture

```
Frontend (React) ←→ Backend (Express) ←→ Database (MongoDB)
     ↓                    ↓                    ↓
  - UI Interface       - REST API           - Storage
  - UX Validation      - Middleware         - Validations
  - Local State        - Error Handling     - Schemas
```

### Data Flow
1. **User** interacts with React interface
2. **Frontend** sends HTTP requests to backend
3. **Backend** processes request and queries MongoDB
4. **Database** returns data
5. **Backend** formats and sends response
6. **Frontend** updates interface

## 🛡️ Validations

### Frontend
- Required fields (name and number)
- Duplicates (confirm update)
- Immediate user feedback

### Backend
- Mongoose Schema validations
- Number format with regex
- Minimum field length
- Centralized error handling

## 🌍 Deployment

The application is deployed on **Render** with:
- **Backend**: Web service with environment variables
- **Database**: MongoDB Atlas
- **Frontend**: Integrated as static files

### Production URL
**[https://phonebook-api-fdwy.onrender.com/](https://phonebook-api-fdwy.onrender.com/)**

## 📸 Screenshots

### Main Interface
![Phonebook Interface](./screenshots/main-interface.png)

### Contact Management
![Contact Management](./screenshots/contact-management.png)

## 🎯 Learning Objectives Achieved

- ✅ Complete REST API development
- ✅ MongoDB database integration
- ✅ Express middleware handling
- ✅ Robust validations with Mongoose
- ✅ Cloud platform deployment
- ✅ Frontend-backend integration
- ✅ Error handling and logging
- ✅ Development best practices

## 📚 Resources and References

- [Full Stack Open Course](https://fullstackopen.com/)
- [Express.js Documentation](https://expressjs.com/)
- [Mongoose Documentation](https://mongoosejs.com/)
- [MongoDB Atlas](https://cloud.mongodb.com/)
- [React Documentation](https://react.dev/)

## 🤝 Let's Connect

If you have questions about the project or want to connect:

- **GitHub**: [@tortilicious](https://github.com/tortilicious)
- **LinkedIn**: https://www.linkedin.com/in/miguel-lozano-cerrada/
- **Portfolio**: [github.com/tortilicious/portfolio](https://github.com/tortilicious/portfolio)

## 📄 License

This project was developed for educational purposes as part of the Full Stack Open course.

---

⭐ **If you liked this project, give it a star!** ⭐
