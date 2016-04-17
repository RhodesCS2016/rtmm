package org.rtmm.prac1;

/* ------------------
Client
usage: java Client [Server hostname] [Server RTSP listening port] [Video file requested]
---------------------- */

import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

public class RTSPClient {

	//GUI
	//----
	JFrame f = new JFrame("Client");
	JButton optionButton = new JButton("Option");
	JButton describeButton = new JButton("Describe");
	JButton setupButton = new JButton("Setup");
	JButton metaButton = new JButton("Get Metadata");
	JButton playButton = new JButton("Play");
	JButton pauseButton = new JButton("Pause");
	JButton tearButton = new JButton("Teardown");
	JPanel mainPanel = new JPanel();
	JPanel buttonPanel = new JPanel();
	JLabel iconLabel = new JLabel();
	ImageIcon icon;


	//RTP variables:
	//----------------
	DatagramPacket rcvdp; //UDP packet received from the server
	DatagramSocket RTPsocket; //socket to be used to send and receive UDP packets
	static int RTP_RCV_PORT = 25000; //port where the client will receive the RTP packets

	Timer timer; //timer used to receive data from the UDP socket
	byte[] buf; //buffer used to store data received from the server 

	//RTSP variables
	//----------------
	//rtsp states 

	static RTSPState state; //RTSP state == INIT or READY or PLAYING
	Socket RTSPsocket; //socket used to send/receive RTSP messages

	//input and output stream filters
	static BufferedReader RTSPBufferedReader;
	static BufferedWriter RTSPBufferedWriter;
	static String VideoFileName; //video file to request to the server
	static String meta_url;
	int RTSPSeqNb = 0; //Sequence number of RTSP messages within the session
	int RTSPid = 0; //ID of the RTSP session (given by the RTSP Server)

	final static String CRLF = "\r\n";
	
	ASFMetadata meta;

	//Video constants:
	//------------------
	static int MJPEG_TYPE = 26; //RTP payload type for MJPEG video

	//--------------------------
	//Constructor
	//--------------------------
	public RTSPClient() {

		//build GUI
		//--------------------------

		//Frame
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		//Buttons
		metaButton.setPreferredSize(new Dimension(120, 40));
		buttonPanel.setLayout(new GridLayout(1,0));
		buttonPanel.add(optionButton);
		buttonPanel.add(describeButton);
		buttonPanel.add(metaButton);
		buttonPanel.add(setupButton);
		buttonPanel.add(playButton);
		buttonPanel.add(pauseButton);
		buttonPanel.add(tearButton);
		optionButton.addActionListener(new optionButtonListener());
		describeButton.addActionListener(new describeButtonListener());
		metaButton.addActionListener(new metaButtonListener());
		setupButton.addActionListener(new setupButtonListener());
		playButton.addActionListener(new playButtonListener());
		pauseButton.addActionListener(new pauseButtonListener());
		tearButton.addActionListener(new tearButtonListener());
		
		optionButton.setEnabled(false);
		describeButton.setEnabled(false);
		setupButton.setEnabled(false);
		playButton.setEnabled(false);
		pauseButton.setEnabled(false);
		tearButton.setEnabled(false);

		//Image display label
		iconLabel.setIcon(null);

		//frame layout
		mainPanel.setLayout(null);
		mainPanel.add(iconLabel);
		mainPanel.add(buttonPanel);
		iconLabel.setBounds(0,0,380,280);
		buttonPanel.setBounds(0,280,790,50);

		f.getContentPane().add(mainPanel, BorderLayout.CENTER);
		f.setSize(new Dimension(800,370));
		f.setVisible(true);

		//init timer
		//--------------------------
		timer = new Timer(20, new timerListener());
		timer.setInitialDelay(0);
		timer.setCoalesce(true);

		//allocate enough memory for the buffer used to receive data from the server
		buf = new byte[15000];    
	}

	//------------------------------------
	//main
	//------------------------------------
	public static void main(String argv[]) throws Exception
	{
		//Create a Client object
		meta_url = argv[0];
		new RTSPClient();
	}


	//------------------------------------
	//Handler for buttons
	//------------------------------------

	//.............
	//TO COMPLETE
	//.............

	//Handler for Setup button
	//-----------------------
	class setupButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){

			//System.out.println("Setup Button pressed !");      

			if (state == RTSPState.INIT) 
			{
				//Init non-blocking RTPsocket that will be used to receive data
				try{
					//construct a new DatagramSocket to receive RTP packets from the server, on port RTP_RCV_PORT
					RTPsocket = new DatagramSocket(RTP_RCV_PORT);

					//set TimeOut value of the socket to 5msec.
					timer.setDelay(5);
				}
				catch (SocketException se)
				{
					System.out.println("Socket exception: "+se);
					System.exit(0);
				}

				//init RTSP sequence number
				RTSPSeqNb = 1;

				//Send SETUP message to the server
				send_RTSP_request(RTSPRequest.SETUP);

				//Wait for the response 
				if (parse_server_response() != 200)
					System.err.println("Invalid Server Response");
				else 
				{
					//change RTSP state and print new state 
					state = RTSPState.READY;
					System.out.println("New RTSP state: READY" + CRLF);
					playButton.setEnabled(true);
					tearButton.setEnabled(true);
					setupButton.setEnabled(false);
					optionButton.setEnabled(true);
					describeButton.setEnabled(true);
				}
			}//else if state != INIT then do nothing

		}
	}

	//Handler for Play button
	//-----------------------
	class playButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e){

			//System.out.println("Play Button pressed !"); 

			if (state == RTSPState.READY) 
			{
				//increase RTSP sequence number
				RTSPSeqNb++;


				//Send PLAY message to the server
				send_RTSP_request(RTSPRequest.PLAY);

				//Wait for the response 
				if (parse_server_response() != 200)
					System.err.println("Invalid Server Response");
				else 
				{
					//change RTSP state and print out new state
					state = RTSPState.PLAYING;
					System.out.println("New RTSP state: PLAYING"  + CRLF);
					
					playButton.setEnabled(false);
					pauseButton.setEnabled(true);

					//start the timer
					timer.start();
				}
			}//else if state != READY then do nothing
		}
	}


	//Handler for Pause button
	//-----------------------
	class pauseButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e){

			//System.out.println("Pause Button pressed !");   

			if (state == RTSPState.PLAYING) 
			{
				//increase RTSP sequence number
				RTSPSeqNb++;

				//Send PAUSE message to the server
				send_RTSP_request(RTSPRequest.PAUSE);

				//Wait for the response 
				if (parse_server_response() != 200)
					System.err.println("Invalid Server Response");
				else 
				{
					//change RTSP state and print out new state
					state = RTSPState.READY;
					System.out.println("New RTSP state: READY"  + CRLF);
					
					pauseButton.setEnabled(false);
					playButton.setEnabled(true);

					//stop the timer
					timer.stop();
				}
			}
			//else if state != PLAYING then do nothing
		}
	}

	//Handler for Teardown button
	//-----------------------
	class tearButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e){

			System.out.println("Teardown Button pressed !");  

			//increase RTSP sequence number
			RTSPSeqNb++;


			//Send TEARDOWN message to the server
			send_RTSP_request(RTSPRequest.TEARDOWN);

			//Wait for the response 
			if (parse_server_response() != 200)
				System.err.println("Invalid Server Response");
			else 
			{     
				//change RTSP state and print out new state
				state = RTSPState.INIT;
				System.out.println("New RTSP state: TEARDOWN"  + CRLF);

				//stop the timer
				timer.stop();

				//exit
				System.exit(0);
			}
		}
	}
	
	class metaButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.err.println("Getting Metadata");
			
			try {
				getMetadata(meta_url);
			} catch (MalformedURLException mue) {
				mue.printStackTrace();
				System.exit(1);
			} catch (IOException ioe) {
				ioe.printStackTrace();
				meta = MetadataParser.parse("<ASX VERSION=\"3.0\">"
						+ "<Title>Streaming Media Url for sample file</Title>"
						+ "<ENTRY>"
						+ "<REF HREF=\"rtsp://127.0.0.1:5000/movie.mjpeg\" />"
						+ "</ENTRY>" 
						+ "</ASX>"
				);
			}
			
			try {
				URI metadata = new URI(meta.getRefs().getFirst());
				
				System.out.println(metadata.getHost());
				System.out.println(metadata.getPort());
				
				RTSPsocket = new Socket(InetAddress.getByName(metadata.getHost()), metadata.getPort());
				
				//Set input and output stream filters:
				RTSPBufferedReader = new BufferedReader(new InputStreamReader(RTSPsocket.getInputStream()) );
				RTSPBufferedWriter = new BufferedWriter(new OutputStreamWriter(RTSPsocket.getOutputStream()) );
				
				VideoFileName = meta.getRefs().getFirst().substring(meta.getRefs().getFirst().lastIndexOf('/') + 1);
				
			} catch (UnknownHostException uhe) {
				uhe.printStackTrace();
			} catch (ConnectException ce) {
				ce.printStackTrace();
			} catch (URISyntaxException use) {
				use.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			
			System.out.println(meta.getTitle());
			System.out.println(meta.getRefs().getFirst());

			//init RTSP state:
			state = RTSPState.INIT;
			
			setupButton.setEnabled(true);
		}
	}
	
	class optionButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			RTSPSeqNb++;
			System.out.println("Getting Options");
			send_RTSP_request(RTSPRequest.OPTIONS);
			
			if (parse_server_response() != 200) {
				System.err.println("Error fetching optioins");
			}
		}
	}
	
	class describeButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			RTSPSeqNb++;
			System.out.println("Getting Describe");
			send_RTSP_request(RTSPRequest.DESCRIBE);
			
			System.out.println("Sent Describe Request");
			
			if (parse_server_response() != 200) {
				System.err.println("Error fetching description");
			}
			
		}
	}


	//------------------------------------
	//Handler for timer
	//------------------------------------

	class timerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			//Construct a DatagramPacket to receive data from the UDP socket
			rcvdp = new DatagramPacket(buf, buf.length);

			try{
				//receive the DP from the socket:
				RTPsocket.receive(rcvdp);

				//create an RTPpacket object from the DP
				RTPpacket rtp_packet = new RTPpacket(rcvdp.getData(), rcvdp.getLength());
				//print important header fields of the RTP packet received: 
				System.out.println("Got RTP packet with SeqNum # "+rtp_packet.getsequencenumber()+" TimeStamp "+rtp_packet.gettimestamp()+" ms, of type "+rtp_packet.getpayloadtype());

				//print header bitstream:
				//rtp_packet.printheader();

				//get the payload bitstream from the RTPpacket object
				int payload_length = rtp_packet.getpayload_length();
				byte [] payload = new byte[payload_length];
				rtp_packet.getpayload(payload);

				//get an Image object from the payload bitstream
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				Image image = toolkit.createImage(payload, 0, payload_length);

				//display the image as an ImageIcon object
				icon = new ImageIcon(image);
				iconLabel.setIcon(icon);
			}
			catch (InterruptedIOException iioe){
				//System.out.println("Nothing to read");
			}
			catch (IOException ioe) {
				System.out.println("Exception caught in client timer: "+ioe);
			}
		}
	}

	//------------------------------------
	//Parse Server Response
	//------------------------------------
	private int parse_server_response() 
	{
		int reply_code = 0;

		try{
			//parse status line and extract the reply_code:
			String StatusLine = RTSPBufferedReader.readLine();
			System.out.println("RTSP Client - Received from Server:");
			System.out.println(StatusLine);

			StringTokenizer tokens = new StringTokenizer(StatusLine);
			tokens.nextToken(); //skip over the RTSP version
			reply_code = Integer.parseInt(tokens.nextToken());

			//if reply code is OK get and print the 2 other lines
			if (reply_code == 200)
			{
				String SeqNumLine = RTSPBufferedReader.readLine();
				System.out.println(SeqNumLine);

				String SessionLine = RTSPBufferedReader.readLine();
				System.out.println(SessionLine);

				//if state == INIT gets the Session Id from the SessionLine
				tokens = new StringTokenizer(SessionLine);
				String header = tokens.nextToken(); //skip over the Session:
				if (header.equals("Date:"))
					for (int i = 0; i<16; i++) {
						System.out.println(RTSPBufferedReader.readLine());
					}
				else if (!header.equals("Public:"))
					RTSPid = Integer.parseInt(tokens.nextToken());
				
				
			}
		}
		catch(Exception ex)
		{
			System.out.println("Exception caught in parse server: "+ex);
			System.exit(0);
		}

		return(reply_code);
	}

	//------------------------------------
	//Send RTSP Request
	//------------------------------------

	//.............
	//TO COMPLETE
	//.............

	private void send_RTSP_request(RTSPRequest request_type)
	{
		try{
			//Use the RTSPBufferedWriter to write to the RTSP socket
			
			String requestLine;
			if (request_type == RTSPRequest.OPTIONS) {
				requestLine = request_type.toString()+" * RTSP/1.0"+CRLF;
			} else {
				requestLine = request_type.toString()+" " + VideoFileName + " RTSP/1.0"+CRLF;
			}
			RTSPBufferedWriter.write(requestLine);
			System.out.print(requestLine);

			//write the CSeq line: 
			String cseqLine = "CSeq: "+this.RTSPSeqNb + CRLF;
			RTSPBufferedWriter.write(cseqLine);
			System.out.println(cseqLine);

			//check if request_type is equal to "SETUP" and in this case write the Transport: line advertising to the server the port used to receive the RTP packets RTP_RCV_PORT
			if (request_type == RTSPRequest.SETUP) {
				String transportLine = "Transport: RTP/UDP; client_port= "+ RTP_RCV_PORT + CRLF;
				RTSPBufferedWriter.write(transportLine);
				System.out.println(transportLine);
			}
			else if (request_type == RTSPRequest.OPTIONS) {
				RTSPBufferedWriter.write(CRLF);
			}
			else if (request_type == RTSPRequest.DESCRIBE) {
				RTSPBufferedWriter.write("Accept: application/sdp");
				RTSPBufferedWriter.write(CRLF);
			}
			else{
				String sessionLine = "Session:"+RTSPid+ CRLF;
				RTSPBufferedWriter.write(sessionLine);
				System.out.println(sessionLine);
			}

			RTSPBufferedWriter.flush();
		} catch (Exception ex) {
			System.out.println("Exception caught in send RTSP request: " + ex);
			System.exit(0);
		}
	}
	
	private void getMetadata(String uri) throws MalformedURLException, ConnectException, IOException {
		URL url = new URL(uri);
		StringBuilder result = new StringBuilder();
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(200);
		BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = r.readLine()) != null) {
			result.append(line).append("/n");
		}
		
		this.meta = MetadataParser.parse(result.toString());
	}

}//end of Class Client
