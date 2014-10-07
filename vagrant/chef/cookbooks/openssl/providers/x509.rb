#
# x509 self signed cert provider
#
# Author:: Jesse Nelson <spheromak@gmail.com>
#
require 'openssl'

use_inline_resources

attr_reader :key_file, :key, :cert, :ef

action :create  do
  unless ::File.exists? new_resource.name
    create_keys

    file new_resource.name do
      action :create_if_missing
      mode  new_resource.mode
      owner new_resource.owner
      group new_resource.group
      content cert.to_pem
    end

    file new_resource.key_file do
      action :create_if_missing
      mode  new_resource.mode
      owner new_resource.owner
      group new_resource.group
      content key.to_pem
    end

  end
end

protected

  def key_file
    unless new_resource.key_file
      path, file= ::File.split(new_resource.name)
      filename  = ::File.basename(file, ::File.extname(file) )
      new_resource.key_file  path + "/" + filename + ".key"
    end
    new_resource.key_file
  end

  def key
    @key ||= if ::File.exists? key_file
               OpenSSL::PKey::RSA.new File.read(key_file), new_resource.key_pass
             else
               OpenSSL::PKey::RSA.new(new_resource.key_length)
             end
    @key
  end

  def cert
    @cert ||= OpenSSL::X509::Certificate.new
  end

  def gen_cert
    cert
    cert.subject = cert.issuer = OpenSSL::X509::Name.parse(subject)
    cert.not_before = Time.now
    cert.not_after = Time.now + (new_resource.expire.to_i * 24 * 60 * 60)
    cert.public_key = key.public_key
    cert.serial = 0x0
    cert.version = 2
  end

  def subject
    @subject ||= "/C="  + new_resource.country +
                 "/O="  + new_resource.org +
                 "/OU=" + new_resource.org_unit +
                 "/CN=" + new_resource.common_name
  end

  def extensions
    [
      ef.create_extension("basicConstraints","CA:TRUE", true),
      ef.create_extension("subjectKeyIdentifier", "hash"),
    ]
  end

  def create_keys
    gen_cert
    @ef ||= OpenSSL::X509::ExtensionFactory.new
    ef.subject_certificate = cert
    ef.issuer_certificate = cert
    cert.extensions = extensions
    cert.add_extension ef.create_extension("authorityKeyIdentifier",
                                       "keyid:always,issuer:always")
    cert.sign key, OpenSSL::Digest::SHA1.new
  end
