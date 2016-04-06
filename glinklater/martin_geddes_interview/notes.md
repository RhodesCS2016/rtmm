# Martin Geddes

## Intro

* Mathematics and Computation
* Worked for Oracle (Telecoms?)
* Self taught

## Distributed Computing

* Even a phone call is an example of distributed Computing
* Networks -> timely delivery of data.
* Machines introduce impairment -> however much less impairment than snail mail.
* Rather than thinking of internet like pipes -> Networks (platforms for trading impairment) are "intelligent resource trading platform".
* how do we share limited resources?
  * The more you try and share a resource, the more interference (impairment) you experience.
  * becomes a balancing act.
  * By allocating resources better we can give more people a better experience.
* best way to share resources.
  * Give people a reserved part of the network?
    * Great for experience, bad for overall resource usage.
  * Best effort networking -> Give multiple people the same resource
    * Great for overall usage, sometimes bad for UX.
    * Introduces contention.
  * "Overprovisioning"
    * Carefully provisioning/engineering resources.
    * Becoming less and less acceptable
      * Due to structure of ISPs changing
      * Due to demand.
      * Trying to schedule better to improve this -> packet scheduling.
      * Existing schedulers use Work Conserving Queues.
        * This is a bad idea apparently.
        * Based on 1970's ideas on how things should work.
      * Better to use individual queues.
  * In order to prioritise time-critical services, you need to first isolate the time-critical services so that you can schedule accordingly.
* Packet networks is a premature industry/technology
  * We were building steam engines in 18th cen. without understanding physics of gasses -> we were building packet networks in 1970s without fully understanding how those would work.

## Net Neutrality

* FCC ruled in favour of net neutrality.
* Traditional set of regulations on common carriage.
  * Draws on regulations for carrying physical packages.
  * recently we have created a trading space as opposed to a simple carriage metaphore.
* Net neutrality tries to specify that all traffic is equal.
* Broadband is stocastic.
* Britain vs. America
  * US has multiple different networks -> Horizon etc.
  * UK has very few tier 0 ISPs
    * many more Tier 2 ISPs
  * Concern about ISPs making unfair resource allocations.
  * There is a need for regulators to come up with an objective system of rules
  * There has to be a way of measuring if service was compliant
  * Needs to be able to enforce.
  * No existing tech to detect net neutrality offenders.
    * Therefore cannot be done.

* Traditionally a broadband network is a set of queues (buffers) at each link.
* New idea
  * Inject packets in such a way that they do not contend with themselves for the rest of their journey.
  * Different quality for different applications.
* Don't think about bandwidth -> think about impairment.
  * Bounding probability of packet loss and delay.
* Does everyone needs to follow this architecture -> No
  * improving one side of a skype call can possibly make call success rate go from 80% to 97%. (?)
* Improvement being made for people doing sign language over video calls.
  * Needs steady frame rates and the codecs need to keep the movement of mouth and hands steady.

## Identifying Packets

* How do we identify the type of traffic in an encrypted packet?
  * We fail to ask it to identify itself?
  * Currently this involves a bit of luck and prayer.
  
