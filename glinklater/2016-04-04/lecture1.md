# Lecture 1

## Different Network Types

* Fixed Networks
  * PSTN
  * ISDN
  * DSL
    * "Bellheads vs Netheads" -> Investigate this.
* Mobile Networks
  * SMS
  * WAP
  * Analogue
  * GSM
  * GPRS
  * UMTS
  * HSDPA
* Internet
  * FTP
  * Email
  * WWW
  * Web 2.0
  * P2P
  * IM
  * VOIP
  * IPTV
  * LBS
  * Triple Play
* Cable Networks
  * Broadcast
  * VoD

## Multi-Media Networking Applications

* Classes of MM Applications:
  1. Stored streaming
    * Files downloaded and stored before being used.
  * life streaming
    * Packets are used as they arrive and are never stored.
  * interactive, real-time.

* Typically these applications are extrememly delay sensitive
  * < 150ms unnoticable
  * 150ms < x < 400ms tolerable
  * > 400ms untolerable
* Loss tolerant: infrequent losses cause minor glitches.
* antithesis of data, which is not loss intolerant but delay tolerant.

## Stored streaming

* Media stored at source and transmitted to client.
* Client playout begins before all data has arrived.
  * Timing conmstraint for still-to-be transmitted data: in time for playout.
  * Problem occurs when too little data is coming in to match the rate at which the data is being played out.
    * Depleting the buffer.

## Internet support of Multi-Media

* Changes made to the internet core are expensive
* Therefore changes are typically made at the application level to make transmission more efficient.

## Reading

* 7.3 -> Making the best of the best effort service
  * limitations of a BES
  * Moving jitter at the audio receiver
* 7.6 -> Providing QoS guarantees.
  * Example
  * Resource reservation.
  * Call admission and setup
  * Guarantted QoS on the Internet.
