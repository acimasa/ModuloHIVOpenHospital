package org.isf.video.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.isf.generaldata.GeneralData;
import org.isf.generaldata.MessageBundle;
import org.isf.patient.gui.PatientInsertExtended;
import org.isf.utils.jobjects.IconButton;
import org.isf.video.manager.VideoDevicesManager;
import org.isf.video.manager.VideoManager;
import org.isf.video.model.Resolution;
import org.isf.video.model.VideoDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatientPhotoPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9129641275344016618L;
	
	private final Logger logger = LoggerFactory.getLogger(PatientInsertExtended.class);

	// Photo Components:
	private JPanel jPhotoPanel = null;
	private String photoFileName = ""; //$NON-NLS-1$
	private VideoFrame videoFrame = null;
	private PhotoPanel externalPanel = null;
	private PatientInsertExtended owner = null;
	
	private JButton jGetPhotoButton = null;
	private JButton higherResolutionButton = null;
	private JButton lowerResolutionButton = null;
	private JButton switchCamButton = null;
	private JLabel lblResolution = null;
	
	private final static int appletMaximumWidth = 200;
	private final static int appletMaximumHeight = 160;
	
	private final static int BUTTONMODE_showStream = 0;
	private final static int BUTTONMODE_shootPhoto = 1;
	
	private static int buttonMode = BUTTONMODE_showStream;
	
	public PatientPhotoPanel(final PatientInsertExtended patientFrame, final int code, final Image patientPhoto) throws IOException {
		owner = patientFrame;
		if (jPhotoPanel == null) {
			jPhotoPanel = new JPanel();
			jPhotoPanel = setMyBorder(jPhotoPanel, MessageBundle.getMessage("angal.patient.patientphoto")); //$NON-NLS-1$
			jPhotoPanel.setLayout(new BorderLayout());
			jPhotoPanel.setBackground(null);
			// jPhotoPanel.setBorder(BorderFactory.createLineBorder(Color.lightGray));

			int buttonLeftRightMargin = 5;

			final Image nophoto = ImageIO.read(new File("rsc/images/nophoto.png")); //$NON-NLS-1$

			final IconButton btnDeletePhoto = new IconButton(new ImageIcon("rsc/icons/delete_button.png")); //$NON-NLS-1$
			btnDeletePhoto.setSize(new Dimension(40, 40));
			btnDeletePhoto.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					int n = JOptionPane.showConfirmDialog(owner, 
							MessageBundle.getMessage("angal.patient.doyoureallywanttodeletepatientsphoto"),  //$NON-NLS-1$
							MessageBundle.getMessage("angal.patient.confirmdeletion"),  //$NON-NLS-1$
							JOptionPane.YES_NO_OPTION);

					if (n == JOptionPane.YES_OPTION) {
						btnDeletePhoto.setVisible(false);
						patientFrame.setPatientPhoto(null);
						externalPanel.updatePhoto(nophoto);
						logger.debug(MessageBundle.getMessage("angal.patient.photodeleted"));
					} else {
						logger.debug(MessageBundle.getMessage("angal.patient.photonotdeleted"));
						return;
					}
				}
			});

			Image photo = patientPhoto;
			boolean patientHasPhoto = photo != null;

			if (!patientHasPhoto) {
				photo = nophoto;
			}

			externalPanel = new PhotoPanel(photo);
			externalPanel.setLayout(new BorderLayout());
			Box box = Box.createHorizontalBox();
			box.add(Box.createHorizontalGlue());

			externalPanel.add(box, BorderLayout.NORTH);

			box.add(btnDeletePhoto);

			if (patientHasPhoto)
				btnDeletePhoto.setVisible(true);
			else
				btnDeletePhoto.setVisible(false);

			GridBagConstraints gbs = new GridBagConstraints();
			gbs.anchor = GridBagConstraints.CENTER;
			
			Box buttonBox1 = Box.createHorizontalBox();
			Box buttonBox2 = Box.createHorizontalBox();
			//jGetPhotoButtonBox.add(Box.createHorizontalStrut(buttonLeftRightMargin));

			if (GeneralData.VIDEOMODULEENABLED) {
				jGetPhotoButton = new JButton(MessageBundle.getMessage("angal.patient.newphoto")); //$NON-NLS-1$
				jGetPhotoButton.setMinimumSize(new Dimension(200, (int) jGetPhotoButton.getPreferredSize().getHeight()));
				jGetPhotoButton.setMaximumSize(new Dimension(200, (int) jGetPhotoButton.getPreferredSize().getHeight()));

				if (GeneralData.DEBUG) {
					if (videoFrame == null)
						videoFrame = new VideoFrame(owner);

					videoFrame.addComponentListener(new java.awt.event.ComponentAdapter() {
						public void componentHidden(java.awt.event.ComponentEvent e) {

							final String filename = videoFrame.selectedPhotoFileName;

							// System.out.println(filename);
							if (filename != null && !filename.equals("")) { //$NON-NLS-1$
								photoFileName = filename;

								new Thread(new Runnable() {
									public void run() {
										btnDeletePhoto.setVisible(true);

										EventQueue.invokeLater(new Runnable() {
											public void run() {
												try {
													Image photo = ImageIO.read(new File(filename));
													
													externalPanel.updatePhoto(photo);
												} catch (IOException ioe) {
													ioe.printStackTrace();
												}
											}
										});
									}
								}).start();
							}
						}
					});
					
					jGetPhotoButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							// aggiorno la lista dei dispositivi
							int videoDevicesCount = VideoManager.updateDeviceList();
	
							// il reset del videomanager serve su linux per
							// interrompere il demone che intercetta il collegamento
							// o la disconnessione di dispositivi
							VideoManager.reset(false);
	
							// se ci sono dispositivi collegati mostriamo il frame
							// altrimenti solo un dialog
							if (videoDevicesCount > 0) {
								new Thread(new Runnable() {
									public void run() {
										videoFrame.init(false);
									}
								}).start();
							} else {
								JOptionPane.showMessageDialog(null, MessageBundle.getMessage("angal.patient.nowebcamfound")); //$NON-NLS-1$
							}
						}
					});
					
					buttonBox1.add(jGetPhotoButton);
				} else {
					VideoManager.init();
					
					Icon switchCamIcon = new ImageIcon("rsc/icons/switchcam.png"); //$NON-NLS-1$
					switchCamButton = new JButton();
					switchCamButton.setIcon(switchCamIcon);
					switchCamButton.addActionListener(new ActionListener(){
						
						public void actionPerformed(ActionEvent e) {
							
							// aggiorno la lista dei dispositivi
							VideoManager.reset(true);
							int videoDevicesCount = VideoManager.updateDeviceList();
							
							if (videoDevicesCount > 0)	{
								logger.debug("idCurrent: " + VideoDevicesManager.idCurrentVideoDevice);
								
								VideoDevice device = VideoManager.getNextVideoDevice(VideoDevicesManager.idCurrentVideoDevice);
								
								if (device != null)	{
									showVideoDeviceStream(device);
								}
							}
						}
					});
					
					jGetPhotoButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							
							switch (buttonMode) {
								case BUTTONMODE_showStream:
									// il reset del videomanager serve su linux per
									// interrompere il demone che intercetta il collegamento
									// o la disconnessione di dispositivi
									VideoManager.reset(true);
									final int videoDevicesCount = VideoManager.updateDeviceList();
									if (videoDevicesCount > 0)	{
										VideoDevice device = VideoDevicesManager.currentVideoDevice;
										if (device == null)	{
											device = VideoManager.getFirstVideoDevice();
										} 
										showVideoDeviceStream(device);
									}
									setMode(BUTTONMODE_shootPhoto);
									break;
								
								case BUTTONMODE_shootPhoto:
									String filename = String.valueOf((new Date()).getTime());
									photoFileName = VideoManager.savePhoto(externalPanel.applet, filename);
									try {
										String tempDir = VideoManager.shotPhotosTempDir;
										String extension = VideoManager.shotPhotosExtension;
										
										String path = tempDir + photoFileName + extension;
										Image photo = ImageIO.read(new File(path));
	
										externalPanel.updatePhoto(photo);
										patientFrame.setPatientPhoto(photo);
									} catch (IOException ioe) {
										ioe.printStackTrace();
									}
									setMode(BUTTONMODE_showStream);
									break;
							}
						}
					});
					
					
					higherResolutionButton = new JButton("+"); //$NON-NLS-1$
					higherResolutionButton.setSize(12, 12);
					
					higherResolutionButton.addActionListener(new ActionListener(){
						
						public void actionPerformed(ActionEvent e) {
							VideoDevice device;
							
							if ((device = VideoDevicesManager.currentVideoDevice) != null)
							{
								Resolution resolution = device.getNextResolution(externalPanel.applet.resolutionWidth,
										externalPanel.applet.resolutionHeight);
								
								if (resolution != null)
								{
									VideoManager.setCurrentResolutionAsDefault(resolution.width, resolution.height, 
											device.deviceIdentificationString);
									
									showVideoDeviceStream(device);
									
									buttonMode = BUTTONMODE_shootPhoto;
								}
							}
						}
					});
					
					lowerResolutionButton = new JButton("-"); //$NON-NLS-1$
					lowerResolutionButton.setSize(12, 12);
					lowerResolutionButton.addActionListener(new ActionListener(){
						
						public void actionPerformed(ActionEvent e) {
							VideoDevice device;
							
							if ((device = VideoDevicesManager.currentVideoDevice) != null)
							{
								Resolution resolution = device.getPreviousResolution(externalPanel.applet.resolutionWidth,
										externalPanel.applet.resolutionHeight);
								
								if (resolution != null)
								{
									VideoManager.setCurrentResolutionAsDefault(resolution.width, resolution.height, 
											device.deviceIdentificationString);
									
									showVideoDeviceStream(device);
									
									buttonMode = BUTTONMODE_shootPhoto;
								}
							}
						}
					});
					
					buttonBox1.add(jGetPhotoButton);
					buttonBox1.add(switchCamButton);
					
					lblResolution = new JLabel(MessageBundle.getMessage("angal.patient.quality")+":"); //$NON-NLS-1$
					buttonBox2.add(lblResolution);
					buttonBox2.add(lowerResolutionButton);
					buttonBox2.add(higherResolutionButton);
					
					setMode(BUTTONMODE_showStream);
				}
				
				buttonBox2.add(Box.createHorizontalStrut(buttonLeftRightMargin));
			}
			

			jPhotoPanel.add(externalPanel, BorderLayout.NORTH);
			jPhotoPanel.add(buttonBox1, java.awt.BorderLayout.CENTER);
			jPhotoPanel.add(buttonBox2, java.awt.BorderLayout.SOUTH);

			jPhotoPanel.setMinimumSize(new Dimension((int) getPreferredSize().getWidth(), 100));
		}
		
		add(jPhotoPanel);
	}

	
	private JPanel setMyBorder(JPanel c, String title) {
		javax.swing.border.Border b1 = BorderFactory.createLineBorder(Color.lightGray);
		/*
		 * javax.swing.border.Border b2 = BorderFactory.createCompoundBorder(
		 * BorderFactory.createTitledBorder(title),null);
		 */
		javax.swing.border.Border b2 = BorderFactory.createTitledBorder(b1, title, javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP);

		c.setBorder(b2);
		return c;
	}
	
	
	public String getPhotoFilePath()
	{
		String tempDir = VideoManager.shotPhotosTempDir;
		String extension = VideoManager.shotPhotosExtension;
		
		StringBuilder path = new StringBuilder(tempDir).append(photoFileName).append(extension);
		
		if (GeneralData.DEBUG) return photoFileName;
		else return path.toString();
	}
	
	
	// creates an applet with the given id and dimensions 
	public void showVideoDeviceStream(final VideoDevice device)
	{
		/*
		 * If another videoDevice stream (or the stream of the selected videoDevice at a different resolution)
		 * is currently being shown, then its applet have to be removed.
		 */
		if ((VideoDeviceStreamAppletManager.currentStreamApplet != null) && (VideoDevicesManager.currentVideoDevice != null))	{
			removeApplet();
		}
		
		device.checkResolutions(false);
		
		final int resolutionWidth;
		final int resolutionHeight;
		
		int defResIndex = device.getDefaultResolutionIndex();
		if (defResIndex != -1)	{
			resolutionWidth = device.getResolutionWidth(defResIndex);
			resolutionHeight = device.getResolutionHeight(defResIndex);
		}
		else	{
			resolutionWidth = device.getResolutionWidth(0);
			resolutionHeight = device.getResolutionHeight(0);
		}
		
		final Dimension appletDimension = calculateAppletDimension(resolutionWidth, resolutionHeight);
		
		//updateStatus(id, "wait", resolutionWidth, resolutionHeight, appletWidth, appletHeight);
		
		/* Switches the two applets in a different thread */
		new Thread (new Runnable() {
			public void run() {
				enableInteraction(false);
				
				final boolean switchOk = VideoDeviceStreamAppletManager
							.switchApplets(device, resolutionWidth, resolutionHeight, 
									appletDimension.width, appletDimension.height);
				
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						if (switchOk)	{
							externalPanel.addApplet(VideoDeviceStreamAppletManager.currentStreamApplet);
							
							//updateCurrentStreamInfo();
						}
						enableInteraction(true);
					}
				});
			}
		}).start();
	}
	
	
	private void removeApplet()	{
		//updateStatus(VideoDevicesManager.idCurrentVideoDevice, "stop", -1, -1, -1, -1);
		
		externalPanel.removeApplet();
	}
	
	
	private Dimension calculateAppletDimension(int resolutionWidth, int resolutionHeight)
	{
		int appletW, appletH;
		
		appletW = appletMaximumWidth;
		appletH = appletMaximumWidth * resolutionHeight / resolutionWidth;
		
		if (appletH > appletMaximumHeight)
		{
			appletH = appletMaximumHeight;
			appletW = appletMaximumHeight * resolutionWidth / resolutionHeight;
		}
		
		System.out.println("Applet dim: " + appletW + "x" + appletH); //$NON-NLS-1$ //$NON-NLS-2$
		
		return (new Dimension(appletW, appletH));
	}
	
	
	private void enableInteraction (boolean enabled)
	{
		jGetPhotoButton.setEnabled(enabled);
		switchCamButton.setEnabled(enabled);
		lowerResolutionButton.setEnabled(enabled);
		higherResolutionButton.setEnabled(enabled);
	}
	
	
	private void setMode (int mode)
	{
		buttonMode = mode;
		
		switch(mode)
		{
			case BUTTONMODE_shootPhoto:
				switchCamButton.setVisible(true);
				lowerResolutionButton.setVisible(true);
				higherResolutionButton.setVisible(true);
				lblResolution.setVisible(true);
				repaint();
				break;
				
			case BUTTONMODE_showStream:
				switchCamButton.setVisible(false);
				lowerResolutionButton.setVisible(false);
				higherResolutionButton.setVisible(false);
				lblResolution.setVisible(false);
				repaint();
				break;
		}
	}
}
