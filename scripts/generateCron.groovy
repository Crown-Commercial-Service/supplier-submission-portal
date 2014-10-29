#!/usr/bin/env groovy
import groovy.xml.MarkupBuilder

def writer = new FileWriter('./war/WEB-INF/cron.xml')

def xml = new MarkupBuilder(writer)
writer.append('<?xml version="1.0" encoding="UTF-8"?>\n')
xml.'cronentries' {
    'cron' {
        'description'('G6 Submissions Backups')
        'url'('/_ah/datastore_admin/backup.create?name=live&amp;amp;kind=Document&amp;kind=Page&amp;kind=listing&amp;filesystem=gs&amp;gs_bucket_name=ssp-live-backups')
        'schedule'('every day 00:00')
        'target'('ah-builtin-python-bundle')
    }
    'cron' {
        'description'('Export of all completed listings to S3')
        'url'('/cron/export')
        'schedule'('every 2 minutes')
    }
}

writer.flush()
