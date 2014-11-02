import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;

/**
 * Description:
 * <br/>Copyright (C), 2014, Some code comes from Yeeku.H.Lee.  HuiWan.
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @author  huiwanqsct@163.com
 * @version  1.0
 */

public class GUI_Image
{
	final int PREVIEW_SIZE = 100;
	JFrame f = new JFrame("����ͼƬ");
	Icon okIcon = new ImageIcon("ico/ok.png");
	JButton ok = new JButton("ȷ��",okIcon);
	JMenuBar menuBar = new JMenuBar();
	JLabel label = new JLabel();
	JFileChooser chooser = new JFileChooser(".");
	JLabel accessory = new JLabel();
	ExtensionFileFilter filter = new ExtensionFileFilter();
	JTextField picture_name = new JTextField(40);
	public void init()
	{
		filter.addExtension("jpg");
		filter.addExtension("jpeg");
		filter.addExtension("gif");
		filter.addExtension("png");
		filter.setDescription("ͼƬ�ļ�(*.jpg,*.jpeg,*.gif,*.png)");
		chooser.addChoosableFileFilter(filter);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileView(new FileIconsView(filter));
		chooser.setAccessory(accessory);
		accessory.setPreferredSize(new Dimension(PREVIEW_SIZE, PREVIEW_SIZE));
		accessory.setBorder(BorderFactory.createEtchedBorder());
		//�ṩ��ͼƬԤ������
		chooser.addPropertyChangeListener(new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent event) 
			{
				//JFileChooser�ı�ѡ�ļ��Ѿ������˸ı�
				if (event.getPropertyName() == JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)
				{
					//��ȡ�û�ѡ������ļ� 
					File f = (File) event.getNewValue();
					if (f == null)
					{ 
						accessory.setIcon(null); 
						return; 
					}
					//�����ļ�����ImageIcon������
					ImageIcon icon = new ImageIcon(f.getPath());
					//���ͼ��̫������С��
					if(icon.getIconWidth() > PREVIEW_SIZE)
					{	
						icon = new ImageIcon(icon.getImage()
							.getScaledInstance(PREVIEW_SIZE, -1, Image.SCALE_DEFAULT));
					}
					//�ı�accessory Label��ͼ��
					accessory.setIcon(icon);
				}
			}
		});

		//ѡ���ļ��ı��¼��Ĵ����Թ���
		JMenu menu = new JMenu("�ļ�");
		menuBar.add(menu);
		JMenuItem openItem = new JMenuItem("��");
		menu.add(openItem);
		//����openItem�˵�����ʾ�����ļ����ĶԻ���
		openItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				int result = chooser.showDialog(f , "��ͼƬ");
				//����û�ѡ����APPROVE����ͬ����ť�����򿪣����漰���Ч��ť
				if(result == JFileChooser.APPROVE_OPTION)
				{
					String name = chooser.getSelectedFile().getPath();
					//��ʾָ��ͼƬ
					label.setIcon(new ImageIcon(name));
				}
			}
		});
		f.setJMenuBar(menuBar);
		
		JPanel bottom = new JPanel();
		bottom.add(picture_name);
		bottom.add(ok);
		f.add(bottom,BorderLayout.SOUTH);
		f.add(new JScrollPane(label));
		ActionListener clickButtonListener = new ActionListener()
		{
			//�����ť�Ժ����Ӧ����
			public void actionPerformed(ActionEvent e)
			{
				picture_name.setText("�û�������ȷ����ť");
			}
		};
		ok.addActionListener(clickButtonListener);
		f.setSize(500, 400);
		//f.pack();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
	public static void main(String[] args)
	{
		JFrame.setDefaultLookAndFeelDecorated(true);
		new GUI_Image().init();
	}
}

//�����ˡ����Java���塱����Ĵ���
class ExtendFileFilter extends FileFilter
{
	private String description = "";
	private ArrayList<String> extensions = new ArrayList<String>();
	public void addExtension(String extension)
	{
		if (!extension.startsWith("."))
		{
			extension = "." + extension;
			extensions.add(extension.toLowerCase());
		}
	} 
	public void setDescription(String aDescription)
	{
		description = aDescription;
	}
	//����ʵ�ֵ��������󷽷�
	public String getDescription()
	{
		return description; 
	}
	public boolean accept(File f)
	{
		//������ļ���·�������ܸ��ļ�
		if (f.isDirectory()) return true;
		//���ļ���תΪСд��ȫ��תΪСд��Ƚϣ����ں����ļ�����Сд��
		String name = f.getName().toLowerCase();
		//�������пɽ��ܵ���չ���������չ����ͬ�����ļ��Ϳɽ��ܡ�
		for (String extension : extensions)
		{
			if (name.endsWith(extension)) 
			{
				return true;
			}
		}
		return false;
	}
}

class FileIconsView extends FileView
{
	private FileFilter filter;
	public FileIconsView(FileFilter filter)
	{
		this.filter = filter;	
	}
	public Icon getIcon(File f)
	{
		if (!f.isDirectory() && filter.accept(f))
		{
			return new ImageIcon("ico/pict.png");
		}
		else if (f.isDirectory())
		{
			//��ȡ���и�·��
			File[] fList = File.listRoots();
			for (File tmp : fList)
			{
				//�����·���Ǹ�·��
				if (tmp.equals(f))
				{
					return  new ImageIcon("ico/dsk.png");
				}
			}
			return new ImageIcon("ico/folder.png");
		}
		//ʹ��Ĭ��ͼ��
		else
		{
			return null;
		}
	}
}