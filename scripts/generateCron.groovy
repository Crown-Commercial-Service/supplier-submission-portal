#!/usr/bin/env groovy
import groovy.xml.MarkupBuilder

def writer = new FileWriter('./war/WEB-INF/cron.xml')

def xml = new MarkupBuilder(writer)
writer.append('<?xml version="1.0" encoding="UTF-8"?>\n')
xml.'cronentries' {

}

writer.flush()
