
actions [ :create ]
default_action :create

attribute :name,       :kind_of => String,  :name_attribute => true
attribute :owner,       :kind_of => String
attribute :group,      :kind_of => String
attribute :expire,     :kind_of => Fixnum
attribute :mode
attribute :org,        :kind_of => String, :required => true
attribute :org_unit,   :kind_of => String, :required => true
attribute :country,    :kind_of => String, :required => true
attribute :common_name, :kind_of => String, :required => true
attribute :key_file,   :kind_of => String, :default => nil
attribute :key_pass,   :kind_of => String, :default => nil
attribute :key_length, :kind_of => Fixnum, :default => 2048
