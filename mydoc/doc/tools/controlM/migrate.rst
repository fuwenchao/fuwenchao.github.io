control-M 迁移步骤
=======================


操作步骤
--------------

1. 找川哥开通网络

2. 找凯哥开通端口, dg ,作快照

3. 验证准生产环境HA,暴力切换与正常切换

4. 准生产切到生产

5. hold 住现生产环境作业,所有任务停止跑批

6. 每组切换一个agent到新生产环境

7. 备份作业到新生产环境

8. 新环境跑批验证


实际操作步骤
------------------

1. hold all job

2. octm-2 shut_ca

3. octm-1 stop 8 9

4. octm-1 ctm_backup_bcp

5. scp back data to nctm-1 /tmp/backup

6. nctm-2 shut_ca

7. nctm-1 stop_all

8. rm -rf nctm-1 backup-db/*dat

9. cp /tmp/backup to nctm-1 backup_db

10. ctm_restore_bcp

11. nctm-1 start_all

12. nctm-2 start_ca

13. ccm check 

14. check parameter

15. check folder , resource , calander

16. agent tranfer; agent ctmagcfg -> 3

17. nctm-1 ctmgetcm

18. Host Group remove

19. test

