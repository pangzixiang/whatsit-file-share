const Constants = {
    uploadURL: process.env.NODE_ENV==='development'? 'http://localhost:12345/upload': "/upload"
}

export default Constants
