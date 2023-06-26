// How to run:
// Open terminal
// > cd src/test/resources/
// > npm i fs
// > node index

let fs = require('fs')

const filePath = 'test-data/chart/'
const files = [
  '1a.json',
  '1b.json',
  '1c.json',
  '1d.json',
  '1f.json',
  '2a.json',
  '2b.json',
  '2d.json',
  '2e.json',
  '2f.json',
  '2g.json',
  '3a.json',
  '3b.json',
  '4a.json',
  '4b.json',
  '4c.json',
  '5a.json',
  '5b.json'
]

const newDir = 'new/'

if (!fs.existsSync(filePath + newDir)) {
  fs.mkdirSync(filePath + newDir)
}

files.map(fileName => {
  let readFileSync = fs.readFileSync(filePath + fileName, 'utf8')
  let contentAsJson = JSON.parse(readFileSync)

  const newStructure = []
  Object.keys(contentAsJson).map(key => {
    newStructure.push({ 'prison': key, data: contentAsJson[key] })
  })

  let stream = fs.createWriteStream(filePath + newDir + fileName)
  stream.once('open', fd => {
    stream.write(JSON.stringify(newStructure))

    stream.end()
  })
  console.log('==>> fileName', fileName, 'Done')

})
