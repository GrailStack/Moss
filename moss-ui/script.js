const inquirer = require('inquirer')
const chalk = require('chalk')
const fs = require('fs')
const archiver = require('archiver')
const child_process = require('child_process')
const filesize = require('filesize')

inquirer
  .prompt([
    {
      type: 'list',
      name: 'action',
      message: '请选择你要进行的操作?',
      choices: [
        { name: '打包+压缩', value: 'build|打包+压缩' },
        { name: '开发', value: 'start|开发' },
      ],
    },
  ])
  .then(answers => {
    const [command, msg] = answers.action.split('|')

    console.log(chalk.green('>>>>>> 开始执行 : %s'), msg)
    child_process.execSync(`npm run ${command}`, { stdio: [0, 1, 2] })
    const output = fs.createWriteStream(__dirname + '/dist.zip')
    output.on('close', function() {
      console.log(chalk.green(`压缩完成 : ${filesize(archive.pointer())} `))
    })

    const archive = archiver('zip', {
      zlib: { level: 9 },
    })

    archive.on('error', function(err) {
      throw err
    })
    archive.pipe(output)
    archive.directory('./dist')
    archive.finalize()
    console.log(chalk.green('>>>>>> 执行结束 : %s'), msg)
  })
