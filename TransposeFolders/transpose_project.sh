if [ $# -lt 2 ] ; then
	echo "Please enter source and target dir."
	exit 1
fi

user_source=$1 ; user_target=$2

rm -rf "$user_target" ; mkdir "$user_target"

for i in "$user_source"/* ; do
	new_child=$(basename "$i")
	
	for j in "$i"/* ; do
		new_parent=$(basename "$j")
		
		mkdir -p "$user_target/$new_parent"
		cp -r "$j" "$user_target/$new_parent/$new_child"
	done
done
