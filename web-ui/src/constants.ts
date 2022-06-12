const Constants = {
    uploadURL: process.env.NODE_ENV==='development'? 'http://localhost:12345/api/upload': "/api/upload",
    downloadURL: process.env.NODE_ENV==='development' ? 'http://localhost:12345/api/download': '/api/download',
    downloadWSURL: process.env.NODE_ENV==='development'? 'ws://localhost:12345/api/ws/downloadList': `ws://${window.location.host}/api/ws/downloadList`
}

export default Constants
