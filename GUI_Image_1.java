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
	JFrame f = new JFrame("输入图片");
	Icon okIcon = new ImageIcon("ico/ok.png");
	JButton ok = new JButton("确认",okIcon);
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
		filter.setDescription("图片文件(*.jpg,*.jpeg,*.gif,*.png)");
		chooser.addChoosableFileFilter(filter);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileView(new FileIconsView(filter));
		chooser.setAccessory(accessory);
		accessory.setPreferredSize(new Dimension(PREVIEW_SIZE, PREVIEW_SIZE));
		accessory.setBorder(BorderFactory.createEtchedBorder());
		//提供了图片预览功能
		chooser.addPropertyChangeListener(new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent event) 
			{
				//JFileChooser的被选文件已经发生了改变
				if (event.getPropertyName() == JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)
				{
					//获取用户选择的新文件 
					File f = (File) event.getNewValue();
					if (f == null)
					{ 
						accessory.setIcon(null); 
						return; 
					}
					//将所文件读入ImageIcon对象中
					ImageIcon icon = new ImageIcon(f.getPath());
					//如果图像太大，则缩小它
					if(icon.getIconWidth() > PREVIEW_SIZE)
					{	
						icon = new ImageIcon(icon.getImage()
							.getScaledInstance(PREVIEW_SIZE, -1, Image.SCALE_DEFAULT));
					}
					//改变accessory Label的图标
					accessory.setIcon(icon);
				}
			}
		});

		//选择文件改变事件的处理略过了
		JMenu menu = new JMenu("文件");
		menuBar.add(menu);
		JMenuItem openItem = new JMenuItem("打开");
		menu.add(openItem);
		//单击openItem菜单项显示“打开文件”的对话框
		openItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				int result = chooser.showDialog(f , "打开图片");
				//如果用户选择了APPROVE（赞同）按钮，即打开，保存及其等效按钮
				if(result == JFileChooser.APPROVE_OPTION)
				{
					String name = chooser.getSelectedFile().getPath();
					//显示指定图片
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
			//点击按钮以后的响应方法
			public void actionPerformed(ActionEvent e)
			{
				picture_name.setText("用户单击了确定按钮");
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

//引用了“疯狂Java讲义”里面的代码
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
	//必须实现的两个抽象方法
	public String getDescription()
	{
		return description; 
	}
	public boolean accept(File f)
	{
		//如果该文件是路径，接受该文件
		if (f.isDirectory()) return true;
		//将文件名转为小写（全部转为小写后比较，用于忽略文件名大小写）
		String name = f.getName().toLowerCase();
		//遍历所有可接受的扩展名，如果扩展名相同，该文件就可接受。
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
			//获取所有根路径
			File[] fList = File.listRoots();
			for (File tmp : fList)
			{
				//如果该路径是根路径
				if (tmp.equals(f))
				{
					return  new ImageIcon("ico/dsk.png");
				}
			}
			return new ImageIcon("ico/folder.png");
		}
		//使用默认图标
		else
		{
			return null;
		}
	}
}