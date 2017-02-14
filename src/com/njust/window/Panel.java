package com.njust.window;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.rosuda.REngine.Rserve.RserveException;

import com.njust.helper.RServeConnection;

public class Panel
{
	protected Shell shell;
	private String inputFilePath;
	private String inputFileName;
	private Text text_input;
	private Text text_q;
	private Text text_p;
	private Text text_predict;
	private Text text_real;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public String getInputFilePath()
	{
		return inputFilePath;
	}

	public static void main(String[] args)
	{
		try
		{
			Panel window = new Panel();
			window.open();
		} catch (Exception e1)
		{
			e1.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open()
	{
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
			{
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents()
	{
		shell = new Shell();
		shell.setSize(634, 854);
		shell.setText("基于GARCH（异方差时间序列模型）的价格预测");
		
		Button btn_abs_pacf = new Button(shell, SWT.NONE);
		btn_abs_pacf.setText("ABS(PACF)图");
		btn_abs_pacf.setBounds(108, 727, 80, 27);

		Button btn_build = new Button(shell, SWT.NONE);
		btn_build.setBounds(531, 653, 80, 27);
		btn_build.setText("建模");

		Button btn_open = new Button(shell, SWT.NONE);
		btn_open.setBounds(531, 616, 80, 27);
		btn_open.setText("打开");

		final Label lblNewLabel = new Label(shell, SWT.BORDER);
		lblNewLabel.setBounds(10, 10, 600, 600);

		final FileDialog fileDialog = new FileDialog(shell);

		text_input = new Text(shell, SWT.BORDER | SWT.LEFT);
		text_input.setBounds(97, 616, 417, 27);

		Label label = new Label(shell, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 14,
				SWT.NORMAL));
		label.setAlignment(SWT.CENTER);
		label.setBounds(10, 616, 81, 27);
		label.setText("数据文件");

		text_q = new Text(shell, SWT.BORDER);
		text_q.setText("1");
		text_q.setBounds(498, 655, 16, 23);

		Label lblQ = new Label(shell, SWT.NONE);
		lblQ.setAlignment(SWT.RIGHT);
		lblQ.setBounds(472, 658, 20, 23);
		lblQ.setText(",q=");

		text_p = new Text(shell, SWT.BORDER);
		text_p.setText("1");
		text_p.setBounds(453, 655, 16, 23);
		
		Button btn_predict = new Button(shell, SWT.NONE);
		btn_predict.setText("预测");
		btn_predict.setBounds(531, 760, 80, 27);
		
        final RServeConnection rsc = new RServeConnection();
        rsc.setFolderPath("D", "R-Data");
        
		btn_predict.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				try
				{
					int p, q;
					p = Integer.valueOf(text_p.getText()).intValue();
					q = Integer.valueOf(text_q.getText()).intValue();
					rsc.predict(p, q);// 建模
					rsc.end();
					Display display = Display.getDefault();
					Image image = new Image(display, rsc.getFilePath());
					ImageData data = image.getImageData();
					data = data.scaledTo(600, 600);
					image = new Image(display, data);
					lblNewLabel.setImage(image);
				} catch (Exception ex)
				{
					System.out.println(ex.toString());
				}
			}
		});

		Label lblP = new Label(shell, SWT.NONE);
		lblP.setText("p=");
		lblP.setAlignment(SWT.RIGHT);
		lblP.setBounds(430, 658, 17, 23);
		
		Display display = Display.getDefault();
		Image image = new Image(display, "D://R-Data/wallpaper.jpg");
		ImageData data = image.getImageData();
		data = data.scaledTo(600, 600);
		image = new Image(display, data);
		lblNewLabel.setImage(image);
		
		Label lblGarchpq = new Label(shell, SWT.NONE);
		lblGarchpq.setText("garch(p,q)模型参数:");
		lblGarchpq.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 11, SWT.NORMAL));
		lblGarchpq.setAlignment(SWT.RIGHT);
		lblGarchpq.setBounds(270, 655, 154, 23);
		
		Button btn_acf = new Button(shell, SWT.NONE);
		btn_acf.setText("ACF图");
		btn_acf.setBounds(22, 694, 80, 27);
		
		Button btn_pacf = new Button(shell, SWT.NONE);
		btn_pacf.setText("PACF图");
		btn_pacf.setBounds(108, 694, 80, 27);
		
		Button btn_abs_acf = new Button(shell, SWT.NONE);
		btn_abs_acf.setText("ABS(ACF)图");
		btn_abs_acf.setBounds(22, 727, 80, 27);
		
		Button btn_qq = new Button(shell, SWT.NONE);
		btn_qq.setText("残差QQ图");
		btn_qq.setBounds(22, 760, 80, 27);
		
		Button btn_res = new Button(shell, SWT.NONE);
		btn_res.setText("标准残差");
		btn_res.setBounds(108, 760, 80, 27);
		
		Button btn_next = new Button(shell, SWT.NONE);
		btn_next.setText("下一步长");
		btn_next.setBounds(531, 711, 80, 27);
		
		Label label_3 = new Label(shell, SWT.NONE);
		label_3.setText("差分拟合方差值:");
		label_3.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 11, SWT.NORMAL));
		label_3.setAlignment(SWT.RIGHT);
		label_3.setBounds(280, 729, 119, 23);
		
		text_predict = new Text(shell, SWT.WRAP);
		text_predict.setText("1");
		text_predict.setBounds(405, 732, 64, 17);
		
		Label label_4 = new Label(shell, SWT.NONE);
		label_4.setText("当前实际方差值:");
		label_4.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 11, SWT.NORMAL));
		label_4.setAlignment(SWT.RIGHT);
		label_4.setBounds(278, 764, 121, 23);
		
		text_real = new Text(shell, SWT.WRAP);
		text_real.setText("1");
		text_real.setBounds(405, 766, 64, 17);
		
		Label label_1 = new Label(shell, SWT.BORDER | SWT.CENTER);
		label_1.setText("模型诊断");
		label_1.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 14, SWT.NORMAL));
		label_1.setAlignment(SWT.CENTER);
		label_1.setBounds(10, 660, 189, 145);
		
		Label label_2 = new Label(shell, SWT.BORDER | SWT.CENTER);
		label_2.setText("预测分析");
		label_2.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 14, SWT.NORMAL));
		label_2.setAlignment(SWT.CENTER);
		label_2.setBounds(270, 691, 244, 114);


		btn_build.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				try
				{
					int p, q;
					p = Integer.valueOf(text_p.getText()).intValue();
					q = Integer.valueOf(text_q.getText()).intValue();
					rsc.read(inputFilePath, inputFileName);
					rsc.build(p, q);// 建模
					Display display = Display.getDefault();
					Image image = new Image(display, rsc.getFilePath());
					ImageData data = image.getImageData();
					data = data.scaledTo(600, 600);
					image = new Image(display, data);
					lblNewLabel.setImage(image);
				} catch (Exception ex)
				{
					System.out.println(ex.toString());
				}
			}
		});

		btn_open.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				fileDialog.open();

				if (!fileDialog.getFileName().isEmpty())
				{
					inputFilePath = fileDialog.getFilterPath() + "\\"
							+ fileDialog.getFileName();
					inputFileName = fileDialog.getFileName().replace(".json",
							"");
				} else
					inputFilePath = "";
				text_input.setText(inputFilePath);
				inputFilePath = inputFilePath.replace("\\", "/");
				System.out.println("FilePath: " + inputFilePath);
				try
				{
					rsc.start();
				} catch (RserveException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}
}
