@echo off
cd c:\TTPROJECT\ETPMS\BACKEND\ETPMS
call mvnw.cmd spring-boot:run > backend_log.txt 2>&1
