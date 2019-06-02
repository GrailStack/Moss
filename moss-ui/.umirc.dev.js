const yaml = require('js-yaml');
const fs = require('fs');
const applicationYml = '../moss-web/src/main/resources/config/application.yml';
try {
  var application = yaml.safeLoad(fs.readFileSync(applicationYml, 'utf8'));
  console.log(application.server.port);
} catch (e) {
  console.log(e);
}
export default {
  define: {
    ENV: 'development',
    apiHost: 'http://localhost:' + application.server.port,
  }
}
