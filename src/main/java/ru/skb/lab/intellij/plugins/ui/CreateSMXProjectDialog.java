package ru.skb.lab.intellij.plugins.ui;

import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiDirectory;
import com.intellij.ui.CollectionComboBoxModel;
import com.intellij.ui.SimpleListCellRenderer;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class CreateSMXProjectDialog extends DialogWrapper {
    private JTextField myGroupIdField;
    private JTextField myArtifactIdField;
    private JTextField myVersionField;
    private JPanel myMainPanel;
    private JComboBox myDeployProfileField;
    private JComboBox myJavaTypeField;
    private JTextField myProjectPackageField;
    private JCheckBox createActiveMQComponentCheckBox;
    private JCheckBox createSSLContextParametersCheckBox;
    private JCheckBox createJdbcTemplateCheckBox;
    private JCheckBox createSqlComponentCheckBox;
    private JCheckBox addCryptoProMetricsCheckBox;
    private JCheckBox createDataSourceCheckBox;
    private JCheckBox createKafkaComponentCheckBox;
    private JCheckBox createHttpClientConfigurerCheckBox;
    private JComboBox projectTypeField;
    private JTextArea prifileInfo;
    private JComboBox appTypeField;

    private String k8sBasePackage = "com.itprofix.smxk8s.%s";

    private String investBasePackage = "com.itprofix.investments.integration.%s";

    private Project project;
    PsiDirectory psiDirectory;

    public CreateSMXProjectDialog(@Nullable Project project, PsiDirectory psiDirectory) {
        super(project);
        this.project=project;
        this.psiDirectory=psiDirectory;
        setTitle("Create smx project");
        init();
        myGroupIdField.setText("");
        myArtifactIdField.setText("");
        myVersionField.setText("1.0.0-SNAPSHOT");

        setModel(myDeployProfileField, ProfileType.values());
        myJavaTypeField.setRenderer(SimpleListCellRenderer.create("", value -> ((ProfileType)value).getPresentableName()));
        myDeployProfileField.setSelectedItem(ProfileType.DEFAULT);
        prifileInfo.setText(ProfileType.DEFAULT.getDescr());
        myDeployProfileField.addActionListener(e -> prifileInfo.setText(((ProfileType) Objects.requireNonNull(myDeployProfileField.getSelectedItem())).getDescr()));

        setModel(myJavaTypeField, JavaType.values());
        myJavaTypeField.setRenderer(SimpleListCellRenderer.create("", value -> ((JavaType)value).getPresentableName()));
        myJavaTypeField.setSelectedItem(JavaType.JAVA_11);
        myJavaTypeField.addActionListener(javaTypeListener);

        setModel(projectTypeField, ProjectType.values());
        projectTypeField.setRenderer(SimpleListCellRenderer.create("", value -> ((ProjectType)value).getPresentableName()));
        projectTypeField.addActionListener(projectTypeListener);

        setModel(appTypeField, AppType.values());
        appTypeField.setRenderer(SimpleListCellRenderer.create("", value -> ((AppType)value).getPresentableName()));
        appTypeField.addActionListener(appTypeListener);

    }

    private <T extends Enum<T> > void setModel (JComboBox field, T[] types) {
        final CollectionComboBoxModel<T> appModel = new CollectionComboBoxModel<>();
        for (T type : types) {
            appModel.add(type);
        }
        //field.setRenderer(SimpleListCellRenderer.create(defaultValue, value -> value.));
        field.setModel(appModel);
    }

    @Override
    public void doCancelAction() {
        //Messages.showMessageDialog(project.getProjectFilePath(), "doCancelAction", Messages.getInformationIcon());
        super.doCancelAction();
    }

    @Override
    protected void doOKAction() {
        //Messages.showMessageDialog(project.getProjectFilePath(), "doOKAction", Messages.getInformationIcon());
        super.doOKAction();
    }

    public Map<String, Object> getProperties() {

        Map<String, Object> additionalProperties = new HashMap<>();

        Properties defaultProperties = FileTemplateManager.getInstance(project).getDefaultProperties();
        for (Map.Entry<Object, Object> entry : defaultProperties.entrySet()) {
            additionalProperties.put(entry.getKey().toString(), entry.getValue());
        }

        additionalProperties.put("GROUP_ID", myGroupIdField.getText());
        additionalProperties.put("ARTIFACT_ID", myArtifactIdField.getText());
        additionalProperties.put("VERTION", myVersionField.getText());
        additionalProperties.put("DEPLOY_PROFILE", ((ProfileType)myDeployProfileField.getSelectedItem()).getValue());
        additionalProperties.put("JAVA_VERSION", ((JavaType)myJavaTypeField.getSelectedItem()).getValue());
        additionalProperties.put("PROJECT_PACKAGE", myProjectPackageField.getText());
        additionalProperties.put("PROJECT_TYPE", ((ProjectType)projectTypeField.getSelectedItem()).getValue());
        additionalProperties.put("APP_TYPE", ((AppType)appTypeField.getSelectedItem()).getValue());

        String camelContextName = ((AppType)appTypeField.getSelectedItem()).getValue().toUpperCase() + "." +
                myArtifactIdField.getText().replace("-",".").replace("_",".").toUpperCase();
        additionalProperties.put("CAMELCONTEXT_NAME", camelContextName);

        additionalProperties.put("ACTIVEMQ_ENABLED", String.valueOf(createActiveMQComponentCheckBox.isSelected()));
        additionalProperties.put("SSLCONTEXT_ENABLED", String.valueOf(createSSLContextParametersCheckBox.isSelected()));
        additionalProperties.put("JDBCTEMPLATE_ENABLED", String.valueOf(createJdbcTemplateCheckBox.isSelected()));
        additionalProperties.put("SQLCOMPONENT_ENABLED", String.valueOf(createSqlComponentCheckBox.isSelected()));
        additionalProperties.put("CRYPTOPRO_METRICS_ENABLED", String.valueOf(addCryptoProMetricsCheckBox.isSelected()));
        additionalProperties.put("DATASOURCE_ENABLED", String.valueOf(createDataSourceCheckBox.isSelected()));
        additionalProperties.put("KAFKACOMPONENT_ENABLED", String.valueOf(createKafkaComponentCheckBox.isSelected()));
        additionalProperties.put("HTTPCLIENTCONFIGURER_ENABLED", String.valueOf(createHttpClientConfigurerCheckBox.isSelected()));

        return additionalProperties;
    }

    ActionListener projectTypeListener = pl -> {
        if (projectTypeField.getSelectedIndex()>=0) {
            ProjectType projectType = (ProjectType) projectTypeField.getSelectedItem();
            String groupId = "";
            switch (projectType) {
                case SMX_K8S -> {
                    createActiveMQComponentCheckBox.setSelected(false);
                    createSSLContextParametersCheckBox.setSelected(true);
                    createJdbcTemplateCheckBox.setSelected(false);
                    createSqlComponentCheckBox.setSelected(false);
                    addCryptoProMetricsCheckBox.setSelected(false);
                    createDataSourceCheckBox.setSelected(false);
                    createKafkaComponentCheckBox.setSelected(true);
                    createHttpClientConfigurerCheckBox.setSelected(false);
                    if (appTypeField.getSelectedIndex() >= 0) {
                        groupId = String.format(k8sBasePackage, ((AppType) appTypeField.getSelectedItem()).getValue());
                    }
                }
                case SMX_INVEST -> {
                    createActiveMQComponentCheckBox.setSelected(false);
                    createSSLContextParametersCheckBox.setSelected(true);
                    createJdbcTemplateCheckBox.setSelected(false);
                    createSqlComponentCheckBox.setSelected(false);
                    addCryptoProMetricsCheckBox.setSelected(false);
                    createDataSourceCheckBox.setSelected(false);
                    createKafkaComponentCheckBox.setSelected(true);
                    createHttpClientConfigurerCheckBox.setSelected(false);
                    if (appTypeField.getSelectedIndex() >= 0) {
                        groupId = String.format(investBasePackage, ((AppType) appTypeField.getSelectedItem()).getValue());
                    }
                }
            }
            myGroupIdField.setText(groupId);
            if(StringUtils.isNoneBlank(groupId)) {
                myProjectPackageField.setText(groupId+".");
            }
        }
    };

    ActionListener appTypeListener = al -> {
        if (projectTypeField.getSelectedIndex()>=0) {
            String groupId = "";
            ProjectType projectType = (ProjectType) projectTypeField.getSelectedItem();
            switch (projectType) {
                case SMX_K8S :
                    groupId = String.format(k8sBasePackage, ((AppType)appTypeField.getSelectedItem()).getValue());
                    break;
                case SMX_INVEST:
                    groupId = String.format(investBasePackage, ((AppType)appTypeField.getSelectedItem()).getValue());
                    break;
            }

            myGroupIdField.setText(groupId);
            if(StringUtils.isNoneBlank(groupId)) {
                myProjectPackageField.setText(groupId+".");
            }
        }
    };

    ActionListener javaTypeListener = jl -> {
        JavaType javaType = (JavaType) myJavaTypeField.getSelectedItem();

        switch (javaType) {
            case JAVA_CRYPTO_PRO -> {
                addCryptoProMetricsCheckBox.setSelected(true);
                addCryptoProMetricsCheckBox.setEnabled(true);
            }
            default -> {
                addCryptoProMetricsCheckBox.setSelected(false);
                addCryptoProMetricsCheckBox.setEnabled(false);
            }
        }

    };

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return myMainPanel;
    }
}
