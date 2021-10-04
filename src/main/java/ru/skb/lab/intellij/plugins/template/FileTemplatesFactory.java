package ru.skb.lab.intellij.plugins.template;

import com.intellij.icons.AllIcons;
import com.intellij.ide.fileTemplates.FileTemplateDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory;
import com.intellij.ui.IconManager;

import javax.swing.*;

/**
 * 09.11.2020
 *
 * @author SSalnikov
 */
public class FileTemplatesFactory implements FileTemplateGroupDescriptorFactory {

  public FileTemplateGroupDescriptor getFileTemplatesDescriptor() {
    Icon i = IconManager.getInstance().getIcon("/META-INF/actionIcon.svg", FileTemplatesFactory.class);
    FileTemplateGroupDescriptor descriptor = new FileTemplateGroupDescriptor("SKB Lab Tools", i);
    descriptor.addTemplate(new FileTemplateDescriptor("SLT_Class.java", AllIcons.Nodes.Class));
    descriptor.addTemplate(new FileTemplateDescriptor("SLT_Enum.java", AllIcons.Nodes.Enum));
    descriptor.addTemplate(new FileTemplateDescriptor("SLT_Interface.java", AllIcons.Nodes.Interface));
    descriptor.addTemplate(new FileTemplateDescriptor("SLT_Bean.java", AllIcons.Nodes.Class));
    descriptor.addTemplate(new FileTemplateDescriptor("SLT_Main.java", AllIcons.Actions.Run_anything));
    descriptor.addTemplate(new FileTemplateDescriptor("SLT_RouteBuilder.java", AllIcons.Nodes.Class));
    return descriptor;
  }

}