#!/bin/bash

sipp -sf scenarios/register_uac.xml -inf userdb.csv -p 5067 -r 5000 -trace_stat -m 200000 rp.rucus.me
sipp -sf scenarios/callflow_uac.xml -p 5067 -r 50 -trace_stat -m 200000 rp.rucus.me
sipp -sf scenarios/callflow_callhandler.xml -p 34567 rp.rucus.me 
