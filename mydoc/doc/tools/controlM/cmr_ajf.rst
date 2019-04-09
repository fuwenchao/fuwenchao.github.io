Job Statuses(CMR_AJF):

0 Wait for Scheduling Criteria to be evaluated
1 Wait for Confirmation from User
2 Submitted to Agent
3 Not Submitted to Agent
4 Executing
5 Ended
6 Analyzed Post Processing Steps
7 Disappeared
8 Post Processed
9 Not Found
A Waiting to Rerun
B Waiting for From Time
C Wait for In Condition
D Wait for Resource
E Wait for Submission
F Not used by CONTROL-M/Server
G Retry Submission to Agent
H Wait for Group Scheduling
I Failed To Submit
J Job waiting for Odate
K Job is past its Odate
Z Agent Status is Unknown

 

取值为8的时候，作业完成，再进一步判断state为Y或者N， Y为OK，N为NOT OK



Here is an extract from one of my scripts that may help: 

when STATE = '8' and STATUS = 'N' then 'Failed' 
when STATE = '8' and STATUS = 'Y' then 'Successfull' 
when STATE in ('0','1','A','B','C','D','E','G','H') then 'Waiting' 
when STATE = '2' then 'Submitted' 
when STATE = '3' then 'Not Submitted' 
when STATE = '5' then 'Ended' 
when STATE = '6' then 'Post Processing' 
when STATE = '7' then 'Disappeared' 
when STATE = '4' then 'Executing' 
when STATE = 'I' then 'Fail to Submit' 




STATE 
0 Wait for Scheduling Criteria to be evaluated 
1 Wait for Confirmation from user 
2 Submitted to Agent 
3 Not submitted to Agent 
4 Executing 
5 Ended 
6 Analyzed Post Processing Steps 
7 Disappeared 
8 Post Processed 
9 Not Found 
A Waiting to Rerun 
B Waiting for From Time 
C Wait for In Condition 
D Wait for Resource 
E Wait for Submission 
F Not used by CONTROL-M/Server 
G Retry Submission to Agent 
H Wait for Group Scheduling 
I Failed To Submit 
J Job waiting for Odate 
K Job is past its Odate 
Z Agent Status is Unknown 

STATUS 

Y means that the job has completed successfully. 
N means that he has not completed or it has completed unsuccessfully. 




- I need a script to list out the top 10 long running jobs each day and also details of how to run it! 


Hi, 
You can query the cmr_ajf table on the Control/M Server database. 

SELECT applgroup,nodeid,memname,odate,startrun,endrun,jobname,applic, cast(endrun as bigint) - cast(startrun as bigint) as seconds_elapsed 
FROM cmr_ajf order by seconds_elapsed desc LIMIT 10 ; 

You can schedule the query just before the NDP and you have the top 10 long running jobs. 

Hope it helps. 

Fabrizio




- How to extract Job Statistics data from Control-M database

Hi, 

In the table <CMR_RUNINF> you find statistics for the 20 last scheduled jobs with status ENDED-OK 

I use this query to extract some informations: 

Update because bad paste, sorry 


#!/bin/csh 
SQL << EOF | tee /tmp/runinf.log 
SET TERMOUT OFF 
SET UNDERLINE = 
SET SPACE 2 
SET PAGESIZE 80 
SET LINESIZE 132 
SET NEWPAGE 0 
SET WRAP OFF 
SET HEADING ON 
COLUMN JOBNAME FORMAT A15 HEADING " JobName" 
COLUMN NODEID FORMAT A10 HEADING " Nodeid" 
COLUMN ELAPTIME FORMAT A12 HEADING " Start_Time" 
COLUMN TIMESTMP FORMAT A12 HEADING " End_Time" 
COLUMN ELAPTIME FORMAT A12 HEADING " Duration" 
select JOBNAME 
, nodeid 
, to_char(to_timestamp(trim(timestmp) 
, 'YYYYMMDDHH24MISS') - numtodsinterval(elaptime / 100 
, 'SECOND'), 'YYYY-MM-DD HH24:MI:SS') as start_time 
, to_char(to_date(trim(timestmp), 'YYYYMMDDHH24MISS') 
, 'YYYY-MM-DD HH24:MI:SS') as end_time 
, substr(to_char(numtodsinterval(elaptime / 100, 'SECOND') 
, 'HH24:MI'), 12, 8) as duration 
from CMR_RUNINF where CMR_RUNINF.JOBNAME like 'your_jobs' 
order by jobname, start_time; 
EOF 
exit 

Regards 
Walty


http://www.scheduler-usage.com/viewtopic.php?t=123



    SQL -w ctmusr



look at this script working on all controlm server on unix with ctrlm account , resultat format csv : 

#!/bin/ksh -x 
# extraction des traitement dans l'AJF 
datej=`date '+%y%m%d'` 
datejj=`date '+%d/%m/%y'` 
heurej=`date "+%H:%M:%S"` 
homedir="/your home directory" 
cd ${homedir} 

SQL -w999 <<EOF> ${homedir}/datacenter.out 
select DATA_CNTR_NAME from CMS_CMNPRM 
go 
EOF 
datacenter=`cat ${homedir}/datacenter.out | sed 's/ //g'` 
echo "Datacenter : ${datacenter}" 

FIC_TEMP="${homedir}/tmp_${datacenter}_${datej}.out" 
FIC_CSV="${homedir}/traitements_${datacenter}_${datej}.csv" 
rm ${homedir}/datacenter.out 
# extraction SQL (jointure entre l'AJF et la JOBINF sur l'ORDERNO) 

SQL -w999 <<EOF> $FIC_TEMP 
select a.APPLIC,";",a.APPLGROUP,";",a.JOBNAME,";",a.MEMNAME,";",b.STARTRUN,";",b.ENDRUN,";",b.OSCOMPSTAT 
from CMR_AJF a,CMR_JOBINF b 
where a.ORDERNO=b.ORDERNO 
order by APPLIC 
go 
EOF 


# Lecture et mise en forme 

echo "APPLI;GROUPE;JOBNAME;SCRIPT;START_DATE;START_HEURE;END_DATE;END_HEURE;DUREE_seconde;CODE" > $FIC_CSV 
cat $FIC_TEMP | sed 's/ //g' | while read line 
do 
ApP=`echo ${line} | awk -F";" '{print $1}'` 
ApG=`echo ${line} | awk -F";" '{print $2}'` 
JbN=`echo ${line} | awk -F";" '{print $3}'` 
MeM=`echo ${line} | awk -F";" '{print $4}'` 
StA=`echo ${line} | awk -F";" '{print $5}'` 
EnD=`echo ${line} | awk -F";" '{print $6}'` 
JrC=`echo ${line} | awk -F";" '{print $7}'` 

if [ "${StA}" != "" -a "${EnD}" != "" ]; then 
StHeures=`echo ${StA} | awk '{ 
StH=substr($0,9,2) 
printf (StH) 
}'` 
EnHeures=`echo ${EnD} | awk '{ 
EnH=substr($0,9,2) 
print (EnH) 
}'` 
StMin=`echo ${StA} | awk '{ 
StM=substr($0,11,2) 
print (StM) 
}'` 
EnMin=`echo ${EnD} | awk '{ 
EnM=substr($0,11,2) 
print (EnM) 
}'` 
StSec=`echo ${StA} | awk '{ 
StS=substr($0,13,2) 
print (StS) 
}'` 
EnSec=`echo ${EnD} | awk '{ 
EnS=substr($0,13,2) 
print (EnS) 
}'` 



# conversion de l'heure en secondes 
# start 
Sec_heure_start=`expr $StHeures \* 3600` 
Sec_min_start=`expr $StMin \* 60` 
Sec_deb=`expr $Sec_heure_start + $Sec_min_start + $StSec` 
# end 
Sec_heure_end=`expr $EnHeures \* 3600` 
Sec_min_end=`expr $EnMin \* 60` 
Sec_fin=`expr $Sec_heure_end + $Sec_min_end + $EnSec` 
# duree en sec 
Sec_duree=`expr $Sec_fin - $Sec_deb` 
# reconvertion en hh:mm:ss 
Duree=`echo $Sec_duree | awk '{ 
H = int ( $0 / 3600) 
R = $0 - H * 3600 
M = int ( R / 60 ) 
S = R - M * 60 
printf ("%02d:%02d:%02d\n",H,M,S)}'` 
# concatenation de l'enregistrement CSV 
Debut=`echo $StA | nawk '{print (substr($0,7,2))"/"(substr($0,5,2))"/"(substr($0,1,4))}'` 
Debut="${Debut};${StHeures}:${StMin}:${StSec}" 
Fin=`echo $EnD | nawk '{print (substr($0,7,2))"/"(substr($0,5,2))"/"(substr($0,1,4))}'` 
Fin="${Fin};${EnHeures}:${EnMin}:${EnSec}" 
echo "${ApP} ; ${ApG} ; ${JbN} ; ${MeM} ; ${Debut} ; ${Fin} ; ${Sec_duree} ; ${JrC}" >> $FIC_CSV 
#exit 
fi 
done 
rm $FIC_TEMP 
# purge a 7 jours 
find ${homedir}/traitements* -mtime +7 -exec rm {} \; 


regards 
Philmalmaison


http://www.scheduler-usage.com/viewtopic.php?t=123

----------


USE [CTRLM]
GO

-- Find Successors
SELECT JOBNAME,SCHEDTAB,* FROM CMR_AJF
WHERE ORDERNO IN (
SELECT C.ORDERNO FROM CMR_AJF A
INNER JOIN CMR_CON_J B ON A.ORDERNO=B.ORDERNO
INNER JOIN CMR_CON_J C ON C.ROWTYPE = 'I' AND B.CONDNAME=C.CONDNAME
WHERE  B.ROWTYPE='O' AND B.OP='+' AND A.JOBNAME='PSIMED_MPI_MAINTENANCE'
) AND TASKTYPE<>'G'

-- Find Predecessors
SELECT JOBNAME FROM CMR_AJF
WHERE ORDERNO IN (
SELECT C.ORDERNO FROM CMR_AJF A
INNER JOIN CMR_CON_J B ON A.ORDERNO=B.ORDERNO
INNER JOIN CMR_CON_J C ON C.ROWTYPE = 'O' and C.OP = '+' AND B.CONDNAME=C.CONDNAME
WHERE  B.ROWTYPE='I' AND A.JOBNAME='PSIMED_MPI_MAINTENANCE'
) AND TASKTYPE<>'G'



-----------


Simple Control-M Reports
Report to query on Cyclic jobs not run due to waiting on condition.

select ‘Cyclic jobs waiting to be cleared’ as “STATUS”, SCHEDTAB, JOBNAME, ODATE, RUNCOUNT, FROMTIME, UNTIL, CYCLICINT from CMR_AJF WHERE STATE IN (‘C’) and CYCLIC = ‘Y’ and RUNCOUNT = ‘0’ and ODATE >=(SELECT MAX(ODATE) -1 FROM CMR_AJF)

This report is very helpful. Use it to catch those cyclic of filewatcher jobs that didn’t run due to maybe an insteam failure.


------------


Hi Vikas, generally I believe that tables prefixed 'CMS' are the job definitions, and tables prefixed 'CMR' are the active environment tables. CMR_AJF should have every job that is currently on the AJF regardless of odate.



Sorry, to elaborate, CMS_JOBDEF contains the base job definitions, but of course it spreads into multiple related tables such as CMS_SHOUT, CMS_DO etc.


---------


CMR_IOALOG_X where x is an number is the table which contain archived log data of AJF , however for a month it might not be there its depend on how many days you are keeping archive data. 

for creation data which you have referred (number of executed jobs, number of jobs with status N, and so on....) 

you can use reporting facility -- modify the report type TREND ANALYSIS to get these kind of data , by default there is 90 days statistics stored in Control-M for reporting facility . 


---------

ctmpsm   -LISTAJFTAB CTMTABLENAME

t provides a delmited list of the jobs and their status. I then wrote some code to go through the list and alert if any jobs are not completed. Works like a charm for what I need.


-----


Need to know the table which stores total number of failure of an active job


 
Please use Control-M reporting tool and run a execution report, by default you will get 90 days data. if you want to increase it you need to change one EM paramater


----

Programmatic access to Control-M is provided as a standard part of the product since V9. You can use RESTful web service requests either directly or via a node.js cli called "CTM". You should be able to find lots of dicumentaiton by entering Contorl-M Automation API into you favorite search engine. 


---------------



You can use some SQL querys to count your jobs and groups. 

For example : 

Number of groups in AJF: 
select count(orderno) from cmr_ajf where tasktype = 'G'; 

Number of jobs in AJF: 
select count(orderno) from cmr_ajf where tasktype != 'G'; 

Number of jobs ordered today in AJF: 
select count(orderno) from cmr_ajf where tasktype != 'G' and ODATE = '20111128'; 

Number of groups ordered today in AJF: 
select count(orderno) from cmr_ajf where tasktype = 'G' and ODATE = '20111128'; 

Use a script to edit a report, before your "new day procedure" 


-----------


Hello All, 

To provide me with a daily list of the Control-M tasks I created a BMC job that runs right before new day and executes the following command on the Control-M/Server: 

ctmdbcount | grep "CMR_AJF table" | mailx -s "The count of production BMC tasks" <email_address> 

The body of the email I receive has: 

count of records in CMR_AJF table: 1252