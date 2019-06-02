export default {
  method: 'post',
  addAction: "admin/app/add",
  action: 'admin/app/update',
  fields: [
    {
      title: '接入应用修改',
      submit: true,
      fields: [
        {
          fieldName: 'name',
          label: '应用名称',
          option: {
            initialValue: '',
            rules: [{required: true, message: '请输入应用名称'}],
          },
          control: {
            name: 'Input',
            type: 'text',
          },
        },
        {
          fieldName: 'appId',
          label: '应用标识',
          option: {
            initialValue: '',
            rules: [{required: true, message: '请输入应用标识'}],
          },
          control: {
            name: 'Input',
            type: 'text'
          },
        },
        {
          fieldName: 'springApplicationName',
          label: 'Spring ApplicationName',
          option: {
            initialValue: '',
          },
          control: {
            name: 'Input',
            type: 'text',
          },
        },
        {
          fieldName:"repoUrl",
          label: '仓库',
          option: {
            initialValue: '',
          },
          control: {
            name: 'Input',
            type: 'text',
          }
        },
        {
          fieldName: 'projectName',
          label: '归属项目',
          option: {
            initialValue: '',
            rules: [{required: true, message: '请输入归属项目'}],
          },
          control: {
            name: 'Select',
            options: () => {
              return window.Store.getState().application.Project.list.map((opt: KVType) => {
                const option = {
                  key: opt.key,
                  projectkey:  opt.key,
                  label: opt.name + `(${opt.ownerName})`,
                  value: String(opt.name)
                };
                return option;
              })
                .concat({label: '未选择', value: '0'})
            },
            onChange:function(value,el){

            }
          },
        },
        {
          fieldName: 'ownerName',
          label: '负责人',
          option: {
            initialValue: '',
            rules: [{required: true, message: '请输入负责人'}],
          },
          control: {
            name: 'Select',
            options: () => {
              return window.Store.getState().application.UserList.map((opt: KVType) => {
                const option = {
                  key:opt.id,
                  label : opt.name + `(${opt.username})`,
                  value: String(opt.name)
                };
                return option;
              })
                .concat({label: '未选择', value: '0'})
            },
          },
        },
        {
          fieldName: 'status',
          label: '应用状态',
          option: {
            initialValue: '',
            rules: [{required: true, message: '请选择应用状态'}],
          },
          control: {
            name: 'Select',
            options: [
              {label: '全部', value: '5'},
              {label: '创建', value: '0'},
              {label: '开发中', value: '1'},
              {label: '运行中', value: '2'},
              {label: '已下线', value: '3'},
            ],
          },
        },
        {
          fieldName: 'springBootVersion',
          label: 'SpringBoot 版本',
          option: {
            rules: [
              {
                required: true,
                pattern: /[1-9]/, // prettier-ignore
                message: '请选择版本',
              },
            ],
          },
          control: {
            name: 'Select',
            options: () => {
              return window.Store.getState().globalConf.springBootVersion.map((opts: KVType) => {
                opts.label = opts.name;
                opts.value = String(opts.value);
                return opts;
              })
                .concat({label: '未选择', value: '0'})
            },
          },
        },
        {
          fieldName: 'springCloudVersion',
          label: 'SpringCloud 版本',
          option: {
            rules: [
              {
                required: true,
                pattern: /[1-9]/, // prettier-ignore
                message: '请选择版本',
              },
            ],
          },
          control: {
            name: 'Select',
            options: () => {
              return window.Store.getState()
                .globalConf.springCloudVersion.map((opts: KVType) => {
                  opts.label = opts.name
                  opts.value = String(opts.value)
                  return opts
                })
                .concat({label: '未选择', value: '0'})
            },
          },
        },
        {
          fieldName: 'takeOver',
          label: '接入状态',
          option: {
            initialValue: '',
            rules: [{required: true, message: '请选择应用状态'}],
          },
          control: {
            name: 'Select',
            options: [{label: '未接入', value: '0'}, {label: '已接入', value: '1'}],
          },
        }
      ],
    },
  ],
}
