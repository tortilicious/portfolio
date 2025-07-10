//  IMPORTS AND CONFIGURATION
require('dotenv').config()
const express = require('express')
const morgan = require('morgan')
const cors = require('cors')
const errorHandler = require('./middleware/errorHandler')
const unknownEndpoint = require('./middleware/unknownEndpointHandler')

const Contact = require('./models/contact')
const app = express()
app.use(express.json())
app.use(cors())
app.use(express.static('dist'))


//  ======  Morgan middleware CONFIG  ======

//  Custom token for POST requests
morgan.token(
    'body',
    function (req) {
      return JSON.stringify(req.body)
    })


//  Morgan custom settings for POST request
app.use(morgan(':method :url :status - :response-time ms - Request: :body', {
  skip: function (req) {
    return req.method !== 'POST'
  }
}))

//  Morgan setting to log exept for POST requests
app.use(morgan('tiny', {
  skip: function (req) {
    return req.method === 'POST'
  }
}))


//  ================================================

//  CRUD ENDPOINTS

//  ======  CREATE  ======

app.post('/api/contacts', (req, res, next) => {
  const contact = new Contact({
    name: req.body.name,
    number: req.body.number,
  })

  contact.save()
      .then(savedContact => {
        res.json(savedContact)
      })
      .catch(next)
})

//  =====  READ  =====

// get phonebook info
app.get('/api/info', (req, res, next) => {
  Contact.find({})
      .then((contacts) => {
        const phoneNumbers = contacts.length
        const requestDate = new Date().toLocaleString()
        res.send(`
        <div>
          <p>Phonebook has info for ${phoneNumbers} people</p>
          <p>${requestDate}</p>
        </div>
      `)
      })
      .catch(next)
})

//  get all persons data
app.get('/api/contacts', (req, res, next) => {
  Contact.find({})
      .then((contacts) => {
        res.json(contacts)
      })
      .catch(next)
})

//  get one person data
app.get('/api/contacts/:id', (req, res, next) => {  // ← Agregar 'next'
  Contact.findById(req.params.id)
      .then((contact) => {
        if (contact) {
          res.json(contact)
        } else {
          const error = new Error('Error: contact not found')
          error.name = 'NotFound'
          return next(error)
        }
      })
      .catch(next)
})

//  =====  UPDATE  =====
app.put('/api/contacts/:id', (req, res, next) => {
  Contact.findByIdAndUpdate(
      req.params.id,
      {name: req.body.name, number: req.body.number},
      {new: true, runValidators: true}
  )
      .then(updatedContact => {
        if (updatedContact) {
          res.json(updatedContact)
        } else {
          const error = new Error('Contact not found')
          error.name = 'NotFound'
          return next(error)
        }
      })
      .catch(next)
})

//  =====  DELETE  =====

//  delete one person
app.delete('/api/contacts/:id', (req, res, next) => {
  Contact.findByIdAndDelete(req.params.id)
      .then(deletedContact => {
        if (deletedContact) {
          res.status(204).end()
        } else {
          const error = new Error('contact not found')
          error.name = 'NotFound'
          return next(error)
        }
      })
      .catch(next)
})

app.use(unknownEndpoint)
app.use(errorHandler)

//  ================================================

//  SERVER
const PORT = process.env.PORT || 3001
app.listen(PORT, () => {
  console.log(`Server running on port: ${PORT}`)
})

