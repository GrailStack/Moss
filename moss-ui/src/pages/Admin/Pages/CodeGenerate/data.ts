export default {
  method: 'get',
  action: 'https://###/init-project.zip?json=',
  confirm: { title: '请确认提交内容' },
  fields: [
    {
      title: '基础参数',
      fields: [
        {
          fieldName: 'javaVersion',
          label: 'Java 版本',
          option: {
            initialValue: '1.8',
            rules: [{ required: true, message: '请选择程序语言' }],
          },
          control: {
            name: 'Select',
            options: ['1.8', '1.9', '1.10'],
          },
        },
        {
          fieldName: 'summerframeworkVersion',
          label: 'SummerFramework 版本',
          option: {
            initialValue: '2.0.0.RELEASE',
            rules: [{ required: true, message: '请选择程序版本' }],
          },
          control: {
            name: 'Select',
            options: ['1.1.8.RELEASE', '2.0.0.RELEASE'],
          },
        },
        {
          fieldName: 'groupId',
          option: {
            rules: [{ required: true, message: '请输入 Group' }],
          },
          control: {
            name: 'Input',
            type: 'text',
          },
        },
        {
          fieldName: 'artifactId',
          option: {
            rules: [{ required: true, message: '请输入 artifact' }],
          },
          control: {
            name: 'Input',
            type: 'text',
          },
        },
        {
          fieldName: 'projectName',
          label: '工程名',
          option: {
            rules: [{ required: true, message: '请输入工程名' }],
          },
          tip:
            '首字母必须小写，中间只允许出现“-”。例如工程名=payment-ccb，表示用Spring Cloud为CCB(中国建设银行)搭建一套Payment(支付)系统的工程',
          control: {
            name: 'Input',
            type: 'text',
          },
        },
        {
          fieldName: 'basePackage',
          label: '包路径',
          tip:
            '将作为所有Java代码的上层路径。工程名、包路径和子工程类型三者组合起来解析出相关目录和结构规则，例如工程名=payment-ccb，包路径=cn.springcloud，子工程类型=server，那么子工程名为payment-ccb-server，里面类路径为cn.springcloud.payment.ccb.server',
          option: {
            rules: [{ required: true, message: '请输入包路径' }],
          },
          control: {
            name: 'Input',
            type: 'text',
          },
        },
      ],
    },
    {
      title: '选择模板组件',
      dependencies: [
        'javaVersion',
        'summerframeworkVersion',
        'groupId',
        'artifactId',
        'projectName',
        'basePackage',
      ],
      submit: true,
      fields: [
        {
          fieldName: 'metadata.templateModelType',
          label: '应用类别',
          option: {
            rules: [{ required: true, message: '请选择应用类别' }],
          },
          control: {
            name: 'Radio',
            options: [
              { label: '业务服务', value: 'dependent' },
              { label: '独立服务', value: 'independent' },
            ],
          },
        },
        {
          dependencies: 'metadata.templateModelType',
          fieldName: 'metadata.templateModelId',
          label: '模板库',
          option: {
            rules: [{ required: true, message: '请选择模板库' }],
          },
          control: {
            name: 'Radio',
            options:
              'https://33333.com/templates/${metadata.templateModelType}?sf_version=${summerframeworkVersion}',
          },
        },
        {
          fieldName: 'metadata.options',
          label: '可选组件',
          dependencies: [
            'metadata.templateModelId',
            ['metadata.templateModelType', 'equal', 'dependent'],
          ],
          option: {
            initialValue: (fields: any) => {
              const { summerframeworkVersion } = fields;
              if (summerframeworkVersion.indexOf('2.0') !== -1) {
                return ['web', 'web-api'];
              } else {
                return ['monitor', 'configcenter'];
              }
            },
          },
          control: {
            name: 'MyCheckbox',
            extend: (fields: any) => {
              const { summerframeworkVersion } = fields;
              if (summerframeworkVersion.indexOf('2.0') !== -1) {
                return [
                  {
                    value: 'web',
                    label: 'web',
                    disabled: true,
                  },
                  {
                    value: 'web-api',
                    label: 'web-api',
                    disabled: true,
                  },
                ];
              } else {
                return [
                  {
                    value: 'monitor',
                    label: 'monitor',
                    disabled: true,
                  },
                  {
                    value: 'configcenter',
                    label: 'configcenter',
                    disabled: true,
                  },
                ];
              }
            },
            options:
              'https://sss.com/templates/${metadata.templateModelType}/${metadata.templateModelId}?sf_version=${summerframeworkVersion}',
          },
        },
      ],
    },
    {
      title: '数据库反向生成',
      dependencies: [
        ['metadata.templateModelType', 'equal', 'dependent'],
        ['metadata.options', 'include', 'Mybatis'],
        'metadata.templateModelId',
      ],
      fields: [
        {
          fieldName: 'builderProperties.dbType',
          label: '数据库类型',
          option: { rules: [{ required: true }] },
          control: {
            name: 'Select',
            options: ['MySQL'],
          },
        },
        {
          fieldName: 'builderProperties.dbName',
          label: '数据库名称',
          option: { rules: [{ required: true }] },
          control: {
            name: 'Input',
            type: 'text',
          },
        },
        {
          fieldName: 'builderProperties.dbUserName',
          label: '数据库用户名',
          option: { rules: [{ required: true }] },
          control: {
            name: 'Input',
            type: 'text',
          },
        },
        {
          fieldName: 'builderProperties.dbPassword',
          label: '数据库密码',
          option: { rules: [{ required: true }] },
          control: {
            name: 'Input',
            type: 'password',
          },
        },
        {
          fieldName: 'builderProperties.dbUrl',
          label: '数据库地址',
          option: { rules: [{ required: true }] },
          control: {
            name: 'Input',
            type: 'text',
            placeholder: '127.0.0.1:3306',
            addonBefore: 'jdbc:mysql://',
          },
        },
        {
          fieldName: 'builderProperties.ignoreClassPrefix',
          label: '忽略从数据库表生成类的前缀部分',
          control: {
            name: 'Input',
            type: 'text',
          },
        },
        {
          fieldName: 'builderProperties.tables',
          label: '选择数据表',
          control: {
            name: 'MyCheckbox',
            options:
              'https://sssss.com/table-names?json=%7B%22dbName%22:%22${builderProperties.dbName}%22,%22dbUserName%22:%22${builderProperties.dbUserName}%22,%22dbPassword%22:%22${builderProperties.dbPassword}%22,%22dbUrl%22:%22jdbc%3amysql%3a%2f%2f${builderProperties.dbUrl}%22,%22dbType%22:%22${builderProperties.dbType}%22%7D',
          },
        },
      ],
    },
    {
      title: '生成类包',
      dependencies: ['basePackage', 'builderProperties.tables'],
      submit: true,
      fields: [
        {
          fieldName: 'builderProperties.builder.packagePrefix',
          label: '生成类包前缀',
          option: {
            initialValue: '${basePackage}.internal',
          },
          control: {
            name: 'Input',
            type: 'text',
            disabled: true,
          },
        },
        {
          fieldName: 'builderProperties.builder.templates[0].shortName',
          label: 'Entity 类生成',
          option: {
            initialValue: 'ENTITY',
          },
          hidden: true,
          control: {
            name: 'Input',
            type: 'text',
          },
        },
        {
          fieldName: 'builderProperties.builder.templates[0].packageName',
          label: 'Entity 类包名',
          option: {
            initialValue: 'entity',
          },
          hidden: true,
          control: {
            name: 'Input',
            type: 'text',
          },
        },
        {
          fieldName: 'builderProperties.builder.templates[0].filePrefix',
          label: 'Entity 类前缀',
          control: {
            name: 'Input',
            type: 'text',
          },
        },
        {
          fieldName: 'builderProperties.builder.templates[0].fileSuffix',
          label: 'Entity 类后缀',
          option: {
            initialValue: 'Entity',
          },
          control: {
            name: 'Input',
            type: 'text',
          },
        },
        {
          fieldName: 'builderProperties.builder.templates[1].shortName',
          label: 'Mapper 类生成',
          option: {
            initialValue: 'MAPPER',
          },
          hidden: true,
          control: {
            name: 'Input',
            type: 'text',
          },
        },
        {
          fieldName: 'builderProperties.builder.templates[1].packageName',
          label: 'Mapper 类包名',
          option: {
            initialValue: 'mapper',
          },
          hidden: true,
          control: {
            name: 'Input',
            type: 'text',
          },
        },
        {
          fieldName: 'builderProperties.builder.templates[1].filePrefix',
          label: 'Mapper 类前缀',
          control: {
            name: 'Input',
            type: 'text',
          },
        },
        {
          fieldName: 'builderProperties.builder.templates[1].fileSuffix',
          label: 'Mapper 类后缀',
          option: {
            initialValue: 'Mapper',
          },
          control: {
            name: 'Input',
            type: 'text',
          },
        },
      ],
    },
  ],
};
