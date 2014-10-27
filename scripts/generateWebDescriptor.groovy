#!/usr/bin/env groovy
import groovy.xml.MarkupBuilder

if(args.length != 3) {
    println "I need <appName> <cookieKey> <sslEnabled> in that order"
    System.exit(-1)
}

def appName = args[0]
def cookieKey = args[1]
def sslEnabled = args[2]

def writer = new FileWriter('./war/WEB-INF/appengine-web.xml')

def xml = new MarkupBuilder(writer)

xml.'appengine-web-app'( xmlns: 'http://appengine.google.com/ns/1.0') {
    application(appName)
    version('1-0-3')
    'threadsafe' ('true')
    'ssl-enabled'(sslEnabled)
    'system-properties' {
        property(name: 'ssp.cookie.enc', value: cookieKey)
        property(name: 'aws.access.key', value: System.getProperty('aws.access.key', System.getenv('AWS_ACCESS_KEY')))
        property(name: 'aws.secret.access.key', value: System.getProperty('aws.secret.access.key', System.getenv('AWS_SECRET_ACCESS_KEY')))
    }
    'static-error-handlers' {
        handler(file: '/errors/default_error.html')
        handler(file: '/errors/over_quota.html', 'error-code': 'over_quota')
    }
}
writer.flush()
