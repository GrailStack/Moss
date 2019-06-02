export default {
  method: 'post',
  action: '/admin/menu/addOrUpdate',
  fields: [
    {
      title: '菜单修改',
      submit: true,
      fields: [
        {
          fieldName: 'id',
          label: 'ID',
          option: {
            initialValue: '',
          },
          control: {
            name: 'Input',
            type: 'text',
            disabled: true,
          },
        },
        {
          fieldName: 'parentId',
          label: '上级菜单',
          option: {
            initialValue: '',
          },
          control: {
            name: 'Input',
            type: 'text',
            placeholder: '添加一级菜单请留空',
          },
        },
        {
          fieldName: 'name',
          label: '菜单名称',
          option: {
            initialValue: '',
            rules: [{ required: true, message: '请输入菜单名称' }],
          },
          control: {
            name: 'Input',
            type: 'text',
            placeholder: '请输入菜单名称',
          },
        },
        {
          fieldName: 'url',
          label: 'URL',
          option: {
            initialValue: '',
            rules: [{ required: true, message: '请输入URL' }],
          },
          control: {
            name: 'Input',
            type: 'text',
            placeholder: '请输入URL，实为components组件名称',
          },
        },
        {
          fieldName: 'key',
          label: 'KEY',
          option: {
            initialValue: '',
            rules: [{ required: true, message: '请输入key' }],
          },
          control: {
            name: 'Input',
            type: 'text',
            placeholder: '请输入key，实为真实url地址',
          },
        },
        {
          fieldName: 'roles',
          label: '权限点',
          option: {
            initialValue: '',
            rules: [{ required: true, message: '请输入权限点' }],
          },
          control: {
            name: 'Input',
            type: 'text',
            placeholder: '如USER,ADMIN 、ADMIN 、USER',
          },
        },
        {
          fieldName: 'sort',
          label: '排序',
          option: {
            initialValue: '0',
            rules: [{ required: true, message: '请选择排序' }],
          },
          control: {
            name: 'Select',
            options: [
              { label: '0', value: '0' },
              { label: '1', value: '1' },
              { label: '2', value: '2' },
              { label: '3', value: '3' },
              { label: '4', value: '4' },
            ],
          },
        },
        {
          fieldName: 'icon',
          label: '图标',
          option: {
            initialValue: '',
            rules: [{ required: true, message: '请输入icon标识符' }],
          },
          control: {
            name: 'Input',
            type: 'text',
            placeholder: '请输入icon标识符 如：setting',
          },
        },
      ],
    },
  ],
};
