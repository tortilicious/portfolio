require('dotenv').config()
const mongoose = require('mongoose')
mongoose.set('strictQuery', false)

const url = process.env.MONGODB_URI

console.log('connecting to fullStackOpen MongoDB')

mongoose.connect(url)
    .then(() => {
      console.log('connected to MongoDB')
    })
    .catch(err => {
      console.log('Error connecting to MongoDB', err)
    })


const contactSchema = new mongoose.Schema({
  name: {
    type: String,
    required: [true, 'Name is required'],
    minlength: [3, 'Name must be at least 3 characters'],
    trim: true
  },
  number: {
    type: String,
    required: [true, 'Number is required'],
    minlength: [8, 'Number must be at least 8 characters'],
    validate: {
      validator: function(v) {
        return /^[0-9]{2,3}-[0-9]+$/.test(v)
      },
      message: 'Number format should be XX-XXXXXXX or XXX-XXXXXXX'
    },
    trim: true
  }
})

contactSchema.set('toJSON', {
  transform: (document, returnedObject) => {
    returnedObject.id = returnedObject._id.toString()
    delete returnedObject._id
    delete returnedObject.__v
  }
})

module.exports = mongoose.model('Contact', contactSchema)